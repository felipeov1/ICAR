package com.icar.platform.domain.repository.payment.gateway;

import com.icar.platform.domain.model.payment.gateway.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, UUID> {
    @Query("SELECT pt FROM PaymentTransaction pt JOIN FETCH pt.appointment WHERE pt.mercadoPagoPaymentId = :paymentId")
    Optional<PaymentTransaction> findByMercadoPagoPaymentIdWithAppointment(@Param("paymentId") Long paymentId);
}