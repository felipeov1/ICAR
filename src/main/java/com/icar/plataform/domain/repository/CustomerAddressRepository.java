package com.icar.plataform.domain.repository;

import com.icar.plataform.domain.model.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, UUID> {

    // Lista apenas os endereços não deletados
    List<CustomerAddress> findAllByCustomerIdAndDeletedAtIsNull(UUID customerId);

    // Busca específica também considerando o soft delete
    Optional<CustomerAddress> findByIdAndCustomerIdAndDeletedAtIsNull(UUID id, UUID customerId);
}
