package com.icar.platform.application.service.oauth;

import com.icar.platform.domain.enums.AuthProvider;
import com.icar.platform.domain.enums.UserStatus;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.repository.customer.CustomerRepository;
import com.icar.platform.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuth2CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public Customer processOAuth2User(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        if (email == null || email.isEmpty()) {
            throw new BusinessException("E-mail não encontrado no provedor OAuth2.");
        }

        Optional<Customer> customerOptional = customerRepository.findByEmail(email);
        Customer customer;

        if (customerOptional.isPresent()) {
            customer = customerOptional.get();
            if (customer.getAuthProvider() != AuthProvider.GOOGLE) {
                throw new BusinessException("Este e-mail já está cadastrado usando e-mail e senha. Por favor, faça login da forma tradicional.");
            }
            customer.setFullName(oauth2User.getAttribute("name"));

        } else {
            customer = createNewCustomerFromOAuth2User(oauth2User);
        }

        return customerRepository.save(customer);
    }

    private Customer createNewCustomerFromOAuth2User(OAuth2User oauth2User) {
        return Customer.builder()
                .email(oauth2User.getAttribute("email"))
                .fullName(oauth2User.getAttribute("name"))
                .phone(null)
                .password(null)
                .status(UserStatus.ACTIVE)
                .emailVerified(true)
                .authProvider(AuthProvider.GOOGLE)
                .build();
    }
}