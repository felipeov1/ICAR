package com.icar.platform.application.service.customer;

import com.icar.platform.api.dto.request.auth.RegisterCustomerRequest;
import com.icar.platform.api.dto.response.auth.RegisterCustomerResponse;
import com.icar.platform.api.dto.response.auth.EmailVerificationResponse;
import com.icar.platform.api.mapper.customer.CustomerMapper;
import com.icar.platform.domain.enums.UserStatus;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.model.email.EmailVerification;
import com.icar.platform.domain.repository.customer.CustomerRepository;
import com.icar.platform.domain.repository.email.EmailVerificationRepository;
import com.icar.platform.infrastructure.security.utils.TokenGenerator;
import com.icar.platform.infrastructure.validation.validator.customer.CustomerValidator;
import com.icar.platform.application.service.email.EmailService;
import com.icar.platform.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerAuthServiceImpl implements CustomerAuthService {

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
        customerValidator.validateCreate(request);

        if (customerRepository.existsByEmail(request.email())) {
            Customer existing = customerRepository.findByEmail(request.email())
                    .orElseThrow(() -> new BusinessException("Erro ao verificar e-mail"));

            if (!existing.isEmailVerified()) {
                try {
                    emailVerificationRepository.deleteByCustomer(existing);

                    EmailVerification verification = EmailVerification.builder()
                            .customer(existing)
                            .token(UUID.randomUUID().toString())
                            .createdAt(LocalDateTime.now())
                            .expiresAt(LocalDateTime.now().plusHours(1))
                            .build();

                    emailVerificationRepository.save(verification);
                    emailService.sendVerificationEmail(existing.getEmail(), verification.getToken());

                    return customerMapper.toDto(existing);
                } catch (Exception e) {
                    throw new BusinessException("Falha ao reenviar e-mail de verificação: " + e.getMessage());
                }
            }
            throw new BusinessException("Este e-mail já está cadastrado e verificado");
        }
        System.out.println("Tentando criar conta para: " + request.email());
        try {
            Customer customer = customerMapper.toEntity(request);
            customer.setPassword(passwordEncoder.encode(request.password()));
            customer.setStatus(UserStatus.PENDING);

            Customer saved = customerRepository.save(customer);

            EmailVerification verification = EmailVerification.builder()
                    .customer(customer)
                    .token(UUID.randomUUID().toString())
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusHours(1))
                    .build();

            emailVerificationRepository.save(verification);
            emailService.sendVerificationEmail(saved.getEmail(), verification.getToken());

            return customerMapper.toDto(saved);
        } catch (Exception e) {
            throw new BusinessException("Falha ao criar conta: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public EmailVerificationResponse verifyEmail(String token) throws BusinessException {

        EmailVerification verification = emailVerificationRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException("Token de verificação inválido"));

        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Token de verificação expirado");
        }

        Customer customer = verification.getCustomer();

        if (customer.isEmailVerified()) {
            throw new BusinessException("Email já verificado anteriormente");
        }

        customer.setEmailVerified(true);
        customerRepository.save(customer);

        emailVerificationRepository.delete(verification);

        return EmailVerificationResponse.builder()
                .email(customer.getEmail())
                .message("Email verified successfully")
                .success(true)
                .build();
    }
 }