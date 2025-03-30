package com.icar.plataform.service;

import com.icar.plataform.dto.request.AppointmentRequest;
import com.icar.plataform.dto.response.AppointmentResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public interface AppointmentService {
    AppointmentResponse create(AppointmentRequest dto);
    AppointmentResponse findById(UUID id);
    List<AppointmentResponse> findAll(); // Adicione este metodo
    List<AppointmentResponse> findByCustomerId(UUID customerId);
    List<AppointmentResponse> findByCarWashId(UUID carWashId);
    void cancel(UUID id);
    AppointmentResponse reschedule(UUID id, LocalDateTime newDateTime);
}