package com.icar.platform.application.service.customer;

import com.icar.platform.api.dto.request.customer.CustomerAddressRequest;
import com.icar.platform.api.dto.response.customer.CustomerAddressResponse;
import com.icar.platform.api.mapper.customer.CustomerAddressMapper;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.model.customer.CustomerAddress;
import com.icar.platform.domain.repository.customer.CustomerAddressRepository;
import com.icar.platform.domain.repository.customer.CustomerRepository;
import com.icar.platform.shared.exception.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerAddressServiceImpl implements CustomerAddressService {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository addressRepository;
    private final CustomerAddressMapper addressMapper;

    @Override
    public List<CustomerAddressResponse> getAllAddresses(UUID customerId) {
        List<CustomerAddress> addresses = addressRepository.findAllByCustomerIdAndDeletedAtIsNull(customerId);
        return addresses.stream()
                .map(addressMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerAddressResponse addAddress(UUID customerId, CustomerAddressRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("Customer not found"));

        CustomerAddress address = addressMapper.toEntity(request, customer);
        CustomerAddress saved = addressRepository.save(address);
        return addressMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CustomerAddressResponse updateAddress(UUID addressId, CustomerAddressRequest request) {
        CustomerAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessException("Address not found"));

        address.setAddressName(request.addressName());
        address.setZipCode(request.zipCode());
        address.setCity(request.city());
        address.setState(request.state());
        address.setStreet(request.street());
        address.setStreet(request.neighborhood());
        address.setStreetNumber(request.streetNumber());
        address.setAdditionalInstructions(request.additionalInstructions());

        address.setUpdatedAt(ZonedDateTime.now());
        return addressMapper.toResponse(addressRepository.save(address));
    }

    public void softDeleteAddress(UUID customerId, UUID addressId) {
        CustomerAddress address = addressRepository.findByIdAndCustomerIdAndDeletedAtIsNull(addressId, customerId)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        address.setDeletedAt(ZonedDateTime.now());
        addressRepository.save(address);
    }
}

