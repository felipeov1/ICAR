package com.icar.platform.domain.repository.payment.gateway;

import com.icar.platform.domain.model.payment.gateway.CompanyMercadoPagoConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyMercadoPagoConfigRepository extends JpaRepository<CompanyMercadoPagoConfig, UUID> {
    Optional<CompanyMercadoPagoConfig> findByCompanyId(UUID companyId);
}