package com.icar.platform.application.service.customer;

import com.icar.platform.api.dto.request.customer.ChangePasswordRequest;
import com.icar.platform.api.dto.request.customer.UpdateProfileRequest;
import com.icar.platform.api.dto.response.customer.CustomerProfileResponse;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.repository.customer.CustomerRepository;
import com.icar.platform.shared.exception.BusinessException;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerProfileServiceImpl implements CustomerProfileService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public CustomerProfileResponse getProfile(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return new CustomerProfileResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getIdentificationNumber(),
                customer.isEmailVerified(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    public void updateCustomerCpf(UUID customerId, String cpf) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (customer.getIdentificationNumber() == null || customer.getIdentificationNumber().isEmpty()) {
            customer.setIdentificationType("CPF");
            customer.setIdentificationNumber(cpf);
            customerRepository.save(customer);
        }
    }

    @Override
    @Transactional
    public CustomerProfileResponse updateProfile(UUID customerId, UpdateProfileRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        customer.setFullName(request.fullName());
        customer.setPhone(request.phone());
        customer.setUpdatedAt(LocalDateTime.now());

        Customer updated = customerRepository.save(customer);

        return new CustomerProfileResponse(
                updated.getId(),
                updated.getFullName(),
                updated.getEmail(),
                updated.getPhone(),
                updated.getIdentificationNumber(),
                updated.isEmailVerified(),
                updated.getCreatedAt(),
                updated.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    public void changePassword(UUID customerId, ChangePasswordRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (!passwordEncoder.matches(request.currentPassword(), customer.getPassword())) {
            throw new BusinessException("Current password is incorrect");
        }

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new BusinessException("New password and confirmation do not match");
        }

        customer.setPassword(passwordEncoder.encode(request.newPassword()));
        customerRepository.save(customer);
    }
}