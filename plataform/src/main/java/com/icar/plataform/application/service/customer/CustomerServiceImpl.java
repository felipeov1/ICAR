package com.icar.plataform.application.service.customer;

import com.icar.plataform.api.dto.request.CustomerCreateRequest;
import com.icar.plataform.api.dto.response.CustomerCreateResponse;
import com.icar.plataform.api.mapper.CustomerMapper;
import com.icar.plataform.domain.enums.UserStatus;
import com.icar.plataform.domain.model.Customer;
import com.icar.plataform.domain.repository.CustomerRepository;
import com.icar.plataform.infrastructure.validation.validator.customer.CustomerValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerValidator customerValidator;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerValidator customerValidator,
                               CustomerRepository customerRepository,
                               CustomerMapper customerMapper,
                               PasswordEncoder passwordEncoder) {
        this.customerValidator = customerValidator;
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public CustomerCreateResponse create(CustomerCreateRequest request) {
        // Validações completas (formato + regras de negócio)
        customerValidator.validateCreate(request);

        // Criação do cliente
        Customer customer = customerMapper.toEntity(request);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setStatus(UserStatus.ACTIVE);

        // Salvamento
        Customer saved = customerRepository.save(customer);
        return customerMapper.toDto(saved);
    }

}