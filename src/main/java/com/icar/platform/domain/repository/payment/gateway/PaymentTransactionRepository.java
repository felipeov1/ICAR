package com.icar.platform.domain.repository.payment.gateway;

import com.icar.platform.domain.model.payment.gateway.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, UUID> {
    Optional<PaymentTransaction> findByMercadoPagoPaymentId(Long mercadoPagoPaymentId);
    @Query("SELECT pt FROM PaymentTransaction pt JOIN FETCH pt.appointment WHERE pt.mercadoPagoPaymentId = :paymentId")
    Optional<PaymentTransaction> findByMercadoPagoPaymentIdWithAppointment(Long paymentId);
}
