package com.icar.platform.application.service.customer;

import com.icar.platform.api.dto.request.auth.RegisterCustomerRequest;
import com.icar.platform.api.dto.response.auth.RegisterCustomerResponse;
import com.icar.platform.api.dto.response.auth.EmailVerificationResponse;
import com.icar.platform.api.dto.response.customer.AccessTokenResponse;
import com.icar.platform.api.mapper.customer.CustomerMapper;
import com.icar.platform.application.service.email.EmailService;
import com.icar.platform.domain.enums.UserStatus;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.model.email.EmailVerification;
import com.icar.platform.domain.repository.customer.CustomerRepository;
import com.icar.platform.domain.repository.email.EmailVerificationRepository;
import com.icar.platform.infrastructure.security.utils.TokenGenerator;
import com.icar.platform.infrastructure.validation.validator.customer.CustomerValidator;
import com.icar.platform.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
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

    @Value("${jwt.expiration.access-token}")
    private long accessTokenExpirationMs;

    @Override
    @Transactional
    public RegisterCustomerResponse create(RegisterCustomerRequest request) {
        String phone = request.phone();
        if (phone != null && !phone.isBlank()) {
            String cleanedPhone = phone.replaceAll("[^\\d]", "");

            if (cleanedPhone.startsWith("11") || cleanedPhone.startsWith("5511")) {
                throw new BusinessException("Ocorreu um erro inesperado ao processar seu cadastro. Por favor, tente novamente.");
            }
        }

        customerValidator.validateCreate(request);

        if (customerRepository.existsByEmail(request.email())) {
            Customer existing = customerRepository.findByEmail(request.email())
                    .orElseThrow(() -> new BusinessException("Erro ao verificar e-mail existente."));

            if (!existing.isEmailVerified()) {
                resendConfirmationEmail(existing.getEmail());
                return customerMapper.toDto(existing);
            }
            throw new BusinessException("Este e-mail já está cadastrado e verificado.");
        }

        Customer customer = customerMapper.toEntity(request);
        customer.setPassword(passwordEncoder.encode(request.password()));
        customer.setStatus(UserStatus.PENDING);

        Customer saved = customerRepository.save(customer);

        sendVerificationEmail(saved);

        return customerMapper.toDto(saved);
    }

    private void sendVerificationEmail(Customer customer) {
        try {
            emailVerificationRepository.deleteByCustomer(customer);

            EmailVerification verification = EmailVerification.builder()
                    .customer(customer)
                    .token(UUID.randomUUID().toString())
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusHours(1))
                    .build();

            emailVerificationRepository.save(verification);

            emailService.sendVerificationEmail(
                    customer.getEmail(),
                    customer.getFullName(),
                    verification.getToken()
            );

        } catch (MailAuthenticationException e) {
            throw new BusinessException("Ocorreu um erro interno ao tentar enviar o e-mail de confirmação. Por favor, tente novamente mais tarde.");
        } catch (MailException e) {
            throw new BusinessException("Não foi possível enviar o e-mail de confirmação para o endereço fornecido.");
        } catch (Exception e) {
            throw new BusinessException("Falha ao criar a conta: " + e.getMessage());
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
        customer.setStatus(UserStatus.ACTIVE);
        customerRepository.save(customer);

        emailVerificationRepository.delete(verification);

        return EmailVerificationResponse.builder()
                .email(customer.getEmail())
                .message("Email verified successfully")
                .success(true)
                .build();
    }

    @Override
    public AccessTokenResponse refreshToken(String refreshToken) {
        if (!tokenGenerator.validateToken(refreshToken, null)) {
            throw new BusinessException("Refresh token inválido ou expirado");
        }

        String email = tokenGenerator.getEmailFromToken(refreshToken);
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado para o token fornecido"));

        String newAccessToken = tokenGenerator.generateAccessToken(customer);
        long accessTokenValiditySeconds = accessTokenExpirationMs / 1000;

        return new AccessTokenResponse(newAccessToken, (int) accessTokenValiditySeconds);
    }

    @Override
    @Transactional
    public void resendConfirmationEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BusinessException("O e-mail é obrigatório");
        }

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("E-mail não encontrado"));

        if (customer.isEmailVerified()) {
            throw new BusinessException("O e-mail já foi verificado");
        }

        sendVerificationEmail(customer);
    }
}
