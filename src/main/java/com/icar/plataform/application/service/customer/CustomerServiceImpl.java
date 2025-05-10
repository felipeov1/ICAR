package com.icar.plataform.application.service.customer;

import com.icar.plataform.api.dto.request.auth.RegisterCustomerRequest;
import com.icar.plataform.api.dto.response.auth.RegisterCustomerResponse;
import com.icar.plataform.api.dto.response.auth.EmailVerificationResponse;
import com.icar.plataform.api.mapper.customer.CustomerMapper;
import com.icar.plataform.domain.enums.UserStatus;
import com.icar.plataform.domain.model.customer.Customer;
import com.icar.plataform.domain.model.email.EmailVerification;
import com.icar.plataform.domain.repository.customer.CustomerRepository;
import com.icar.plataform.domain.repository.email.EmailVerificationRepository;
import com.icar.plataform.infrastructure.security.utils.TokenGenerator;
import com.icar.plataform.infrastructure.validation.validator.customer.CustomerValidator;
import com.icar.plataform.application.service.email.EmailService;
import com.icar.plataform.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerValidator customerValidator;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenGenerator tokenGenerator;
    private final EmailVerificationRepository emailVerificationRepository;
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 100;


    @Override
    @Transactional
    public RegisterCustomerResponse create(RegisterCustomerRequest request) {
        // Validações completas (formato + regras de negócio)
        customerValidator.validateCreate(request);

        // Criação do cliente
        Customer customer = customerMapper.toEntity(request);
        customer.setPassword(passwordEncoder.encode(request.password()));
        customer.setStatus(UserStatus.PENDING); // Alterado para PENDING

        // Salvamento
        Customer saved = customerRepository.save(customer);

        EmailVerification verification = EmailVerification.builder()
                .customer(customer)
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();

        emailVerificationRepository.save(verification);

        // Envia e-mail
        emailService.sendVerificationEmail(saved.getEmail(), verification.getToken());

        return customerMapper.toDto(saved);
    }

    @Override
    @Transactional
    public EmailVerificationResponse verifyEmail(String token) {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                attempt++;
                return attemptVerifyEmail(token);
            } catch (ObjectOptimisticLockingFailureException e) {
                if (attempt == MAX_RETRIES) {
                    throw new BusinessException("Failed to verify email after multiple attempts. Please try again.");
                }
                sleepForRetry();
            }
        }
        throw new BusinessException("Unexpected error during email verification");
    }

    private EmailVerificationResponse attemptVerifyEmail(String token) {
        EmailVerification verification = emailVerificationRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException("Invalid token"));

        validateVerification(verification);

        Customer customer = verification.getCustomer();
        customer.setStatus(UserStatus.ACTIVE);
        customer.setEmailVerified(true);
        verification.setVerifiedAt(LocalDateTime.now());

        // Salva ambas as entidades atualizadas
        customerRepository.save(customer);
        emailVerificationRepository.save(verification); // ✅ ESSENCIAL

        return EmailVerificationResponse.builder()
                .success(true)
                .message("Email verified successfully")
                .build();
    }

    private void validateVerification(EmailVerification verification) {
        if (verification.isExpired()) {
            throw new BusinessException("Token expired");
        }
        if (verification.isAlreadyVerified()) {
            throw new BusinessException("Token already used");
        }
    }

    private void sleepForRetry() {
        try {
            Thread.sleep(RETRY_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException("Verification process interrupted");
        }
    }
}