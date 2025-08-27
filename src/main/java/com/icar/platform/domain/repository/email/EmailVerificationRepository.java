package com.icar.platform.domain.repository.email;

import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.model.email.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {

    Optional<EmailVerification> findByToken(String token);

    Optional<EmailVerification> findByCustomer(Customer customer);

    @Transactional
    @Modifying
    @Query("DELETE FROM EmailVerification e WHERE e.customer = :customer")
    void deleteByCustomer(@Param("customer") Customer customer);
}