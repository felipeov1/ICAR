package com.icar.platform.application.service.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.CompanyCustomerRequest;
import com.icar.platform.api.dto.response.carwash.profile.CompanyCustomerResponse;
import com.icar.platform.api.mapper.carwash.CompanyCustomerMapper;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.model.carwash.profile.CompanyCustomer;
import com.icar.platform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.platform.domain.repository.carwash.profile.CompanyCustomerRepository;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyCustomerServiceImpl implements CompanyCustomerService {

    private final CompanyCustomerRepository companyCustomerRepository;
    private final CarWashProfileRepository profileRepository;
    private final CompanyCustomerMapper customerMapper;

    @Override
    @Transactional
    public CompanyCustomerResponse create(UUID profileId, CompanyCustomerRequest request) {
        CarWashProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil do Lava-rápido não encontrado com ID: " + profileId));

        CompanyCustomer customer = customerMapper.toEntity(request);
        customer.setProfile(profile);

        CompanyCustomer savedCustomer = companyCustomerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyCustomerResponse> findByProfile(UUID profileId) {
        if (!profileRepository.existsById(profileId)) {
            throw new ResourceNotFoundException("Perfil do Lava-rápido não encontrado com ID: " + profileId);
        }
        return companyCustomerRepository.findByProfileIdAndDeletedAtIsNull(profileId)
                .stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompanyCustomerResponse update(UUID profileId, UUID customerId, CompanyCustomerRequest request) {
        CompanyCustomer customer = companyCustomerRepository.findByProfileIdAndIdAndDeletedAtIsNull(profileId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + customerId + " para este perfil."));

        customer.setFullName(request.fullName());
        customer.setPhone(request.phone());
        customer.setZipCode(request.zipCode());
        customer.setStreet(request.street());
        customer.setStreetNumber(request.streetNumber());
        customer.setNeighborhood(request.neighborhood());
        customer.setCity(request.city());
        customer.setState(request.state());
        customer.setAdditionalInstructions(request.additionalInstructions());

        CompanyCustomer updatedCustomer = companyCustomerRepository.save(customer);

        return customerMapper.toResponse(updatedCustomer);
    }
}
