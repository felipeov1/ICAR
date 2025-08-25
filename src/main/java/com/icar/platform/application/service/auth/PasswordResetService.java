package com.icar.platform.application.service.auth;

import com.icar.platform.application.service.email.EmailService;
import com.icar.platform.domain.model.auth.PasswordResetToken;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.repository.auth.PasswordResetTokenRepository;
import com.icar.platform.domain.repository.carwash.legal.CarWashRegistrationDataRepository;
import com.icar.platform.domain.repository.customer.CustomerRepository;
import com.icar.platform.shared.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final CustomerRepository customerRepository;
    private final CarWashRegistrationDataRepository carWashRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    private static final int EXPIRATION_MINUTES = 60;

    @Transactional
    public void createPasswordResetRequest(String email, String userType) {
        UUID userId;
        String userEmail;
        String userName;

        if ("CARWASH".equalsIgnoreCase(userType)) {
            CarWashRegistration carWash = carWashRepository.findByEmail(email)
                    .orElseThrow(() -> new BusinessException("Nenhuma conta de empresa encontrada com este e-mail."));
            userId = carWash.getId();
            userEmail = carWash.getEmail();
            userName = carWash.getLegalName();
        } else {
            Customer customer = customerRepository.findByEmail(email)
                    .orElseThrow(() -> new BusinessException("Nenhuma conta de cliente encontrada com este e-mail."));
            userId = customer.getId();
            userEmail = customer.getEmail();
            userName = customer.getFullName();
        }

        tokenRepository.deleteByUserIdAndUserType(userId, userType.toUpperCase());

        String tokenValue = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(tokenValue, userId, userType.toUpperCase(), EXPIRATION_MINUTES);
        tokenRepository.save(resetToken);

        String resetLink = frontendUrl + "/redefinir-senha/" + tokenValue;
        emailService.sendPasswordResetEmail(userEmail, userName, resetLink);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException("Token de redefinição inválido."));

        if (resetToken.isExpired()) {
            tokenRepository.delete(resetToken);
            throw new BusinessException("Token de redefinição expirado.");
        }

        if ("CARWASH".equalsIgnoreCase(resetToken.getUserType())) {
            CarWashRegistration carWash = carWashRepository.findById(resetToken.getUserId())
                    .orElseThrow(() -> new BusinessException("Usuário associado ao token não encontrado."));
            carWash.setPassword(passwordEncoder.encode(newPassword));
            carWashRepository.save(carWash);
        } else {
            Customer customer = customerRepository.findById(resetToken.getUserId())
                    .orElseThrow(() -> new BusinessException("Usuário associado ao token não encontrado."));
            customer.setPassword(passwordEncoder.encode(newPassword));
            customerRepository.save(customer);
        }

        tokenRepository.delete(resetToken);
    }
}