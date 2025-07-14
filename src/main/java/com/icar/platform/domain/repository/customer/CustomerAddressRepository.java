package com.icar.platform.domain.repository.customer;

import com.icar.platform.domain.model.customer.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, UUID> {

    List<CustomerAddress> findAllByCustomerIdAndDeletedAtIsNull(UUID customerId);

    Optional<CustomerAddress> findByIdAndCustomerIdAndDeletedAtIsNull(UUID id, UUID customerId);
}
