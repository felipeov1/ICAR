package com.icar.plataform.service;

import com.icar.plataform.dto.request.CustomerRequest;
import com.icar.plataform.dto.response.AppointmentResponse;
import com.icar.plataform.dto.response.CustomerResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CustomerService {
    CustomerResponse create(CustomerRequest dto);
    List<AppointmentResponse> findAppointmentsByCustomer(UUID customerId);
}