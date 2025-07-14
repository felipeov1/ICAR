package com.icar.platform.application.service.appointment;

import com.icar.platform.api.dto.request.appointment.AppointmentRequest;
import com.icar.platform.api.dto.response.appointment.AppointmentResponse;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    AppointmentResponse createAppointment(UUID customerId, AppointmentRequest request);

    AppointmentResponse cancelAppointment(UUID appointmentId, UUID customerId);

    AppointmentResponse rescheduleAppointment(UUID appointmentId, UUID customerId, ZonedDateTime newDateTime);

    AppointmentResponse completeAppointment(UUID appointmentId);

    List<AppointmentResponse> getUpcomingAppointmentsForCarWash(UUID carWashId);
    List<AppointmentResponse> getCompletedAppointmentsForCarWash(UUID carWashId);
    List<AppointmentResponse> getCanceledAppointmentsForCarWash(UUID carWashId);

    List<AppointmentResponse> getUpcomingAppointmentsForCustomer(UUID customerId);
    List<AppointmentResponse> getCompletedOrCanceledAppointmentsForCustomer(UUID customerId);
}