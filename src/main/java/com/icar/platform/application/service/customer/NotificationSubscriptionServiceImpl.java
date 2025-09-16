package com.icar.platform.application.service.customer;

import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.model.notification.DryWashSubscription;
import com.icar.platform.domain.repository.customer.CustomerRepository;
import com.icar.platform.domain.repository.customer.DryWashSubscriptionRepository;
import com.icar.platform.shared.exception.DuplicateEntityException;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationSubscriptionServiceImpl {

    private final DryWashSubscriptionRepository subscriptionRepository;
    private final CustomerRepository customerRepository; // Repositório para buscar o cliente

    @Transactional
    public void subscribeToDryWash(UUID customerId) {
        if (subscriptionRepository.existsByCustomerId(customerId)) {
            throw new DuplicateEntityException("Customer is already subscribed", "dry_wash_subscription", "customer_id");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        DryWashSubscription subscription = new DryWashSubscription();
        subscription.setCustomer(customer);

        subscriptionRepository.save(subscription);
    }
}