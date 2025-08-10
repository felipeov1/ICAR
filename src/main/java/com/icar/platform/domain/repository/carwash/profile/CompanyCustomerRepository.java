package com.icar.platform.domain.repository.carwash.profile;

import com.icar.platform.domain.model.carwash.profile.CompanyCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyCustomerRepository extends JpaRepository<CompanyCustomer, UUID> {
    List<CompanyCustomer> findByProfileIdAndDeletedAtIsNull(UUID profileId);
    Optional<CompanyCustomer> findByProfileIdAndIdAndDeletedAtIsNull(UUID profileId, UUID customerId);

}
