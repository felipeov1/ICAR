package com.icar.plataform.application.service.customer;

import com.icar.plataform.api.dto.request.CustomerAddressRequest;
import com.icar.plataform.api.dto.response.CustomerAddressResponse;
import com.icar.plataform.api.mapper.CustomerAddressMapper;
import com.icar.plataform.domain.model.Customer;
import com.icar.plataform.domain.model.CustomerAddress;
import com.icar.plataform.domain.repository.CustomerAddressRepository;
import com.icar.plataform.domain.repository.CustomerRepository;
import com.icar.plataform.shared.exception.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        address.setNumber(request.number());
        address.setAdditionalInstructions(request.additionalInstructions());

        address.setUpdatedAt(LocalDateTime.now());
        return addressMapper.toResponse(addressRepository.save(address));
    }

    public void softDeleteAddress(UUID customerId, UUID addressId) {
        CustomerAddress address = addressRepository.findByIdAndCustomerIdAndDeletedAtIsNull(addressId, customerId)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        // Valida se está em uso em algum agendamento futuro, se quiser
        // if (agendamentoService.isAddressInUse(address)) {
        //     throw new BusinessException("Endereço está em uso e não pode ser excluído.");
        // }

        address.setDeletedAt(LocalDateTime.now());
        addressRepository.save(address);
    }
}

