package com.icar.plataform.application.service.customer;

import com.icar.plataform.api.dto.request.customer.ChangePasswordRequest;
import com.icar.plataform.api.dto.request.customer.UpdateProfileRequest;
import com.icar.plataform.api.dto.response.customer.CustomerProfileResponse;
import com.icar.plataform.domain.model.customer.Customer;
import com.icar.plataform.domain.repository.customer.CustomerRepository;
import com.icar.plataform.shared.exception.BusinessException;
import com.icar.plataform.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerProfileServiceImpl implements CustomerProfileService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomerProfileResponse getProfile(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return new CustomerProfileResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.isEmailVerified(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
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