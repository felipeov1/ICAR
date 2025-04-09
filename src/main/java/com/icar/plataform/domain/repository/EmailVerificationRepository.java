package com.icar.plataform.domain.repository;

import com.icar.plataform.domain.model.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {
    Optional<EmailVerification> findByToken(String token);
}
