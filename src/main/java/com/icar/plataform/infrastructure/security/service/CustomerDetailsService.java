package com.icar.plataform.infrastructure.security.service;

import com.icar.plataform.domain.enums.UserStatus;
import com.icar.plataform.domain.model.customer.Customer;
import com.icar.plataform.domain.repository.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Customer customer = customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return User.withUsername(customer.getEmail())
                .password(customer.getPassword())
                .roles("CUSTOMER")
                .disabled(customer.getStatus() != UserStatus.ACTIVE)
                .build();
    }
}