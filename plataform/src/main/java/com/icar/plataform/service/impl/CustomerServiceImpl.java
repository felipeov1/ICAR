package com.icar.plataform.service.impl;

import com.icar.plataform.domain.model.Customer;
import com.icar.plataform.dto.request.CustomerRequest;
import com.icar.plataform.dto.response.AppointmentResponse;
import com.icar.plataform.dto.response.CustomerResponse;
import com.icar.plataform.exception.ResourceNotFoundException;
import com.icar.plataform.mapper.CustomerMapper;
import com.icar.plataform.repository.AppointmentRepository;
import com.icar.plataform.repository.CustomerRepository;
import com.icar.plataform.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AppointmentRepository appointmentRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public CustomerResponse create(CustomerRequest dto) {
        Customer customer = customerMapper.toEntity(dto);
        customer.setPassword(passwordEncoder.encode(dto.password()));
        Customer saved = customerRepository.save(customer);
        return customerMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> findAppointmentsByCustomer(UUID customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        return appointmentRepository.findByCustomerId(customerId)
                .stream()
                .map(customerMapper::toAppointmentDto)
                .toList();
    }
}