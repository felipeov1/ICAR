package com.icar.plataform.domain.repository.email;

import com.icar.plataform.domain.model.email.EmailVerification;
import com.icar.plataform.domain.model.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {
    Optional<EmailVerification> findByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE FROM EmailVerification e WHERE e.customer = :customer")
    void deleteByCustomer(Customer customer);
}