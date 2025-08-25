package com.icar.platform.application.service.appointment;

import com.icar.platform.api.dto.request.appointment.AppointmentRequest;
import com.icar.platform.api.dto.request.appointment.ManualAppointmentRequest;
import com.icar.platform.api.dto.request.carwash.profile.RescheduleByCompanyRequest;
import com.icar.platform.api.dto.response.appointment.AppointmentCreationResponse;
import com.icar.platform.api.dto.response.appointment.AppointmentResponse;
import com.icar.platform.api.dto.response.carwash.CompanyAppointmentResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AppointmentService {

    AppointmentCreationResponse createAppointment(UUID customerId, AppointmentRequest request);

    AppointmentResponse cancelAppointment(UUID appointmentId, UUID customerId);

    AppointmentResponse cancelAppointmentByCompany(UUID appointmentId, UUID profileId);
    void deletePendingAppointment(UUID appointmentId, UUID customerId);

    AppointmentResponse rescheduleAppointment(UUID appointmentId, UUID customerId, LocalDateTime newDateTime);
    List<AppointmentResponse> getUpcomingAppointmentsForCustomer(UUID customerId);
    List<AppointmentResponse> getCompletedOrCanceledAppointmentsForCustomer(UUID customerId);
    AppointmentResponse getAppointmentByIdForCustomer(UUID appointmentId, UUID customerId);

    AppointmentResponse completeAppointment(UUID appointmentId);
    AppointmentResponse confirmRefund(UUID appointmentId, UUID profileId);
    List<CompanyAppointmentResponse> getUpcomingAppointmentsForCarWash(UUID profileId);
    List<CompanyAppointmentResponse> getCompletedAppointmentsForCarWash(UUID profileId);
    List<CompanyAppointmentResponse> getCanceledAppointmentsForCarWash(UUID profileId);
    List<CompanyAppointmentResponse> getAppointmentsByMonth(UUID profileId, int year, int month);
    AppointmentResponse createManualAppointment(UUID profileId, ManualAppointmentRequest request);
    List<CompanyAppointmentResponse> getRefundPendingAppointmentsForCarWash(UUID profileId);
    Map<String, Long> getRefundPendingCountForCarWash(UUID profileId);
    AppointmentResponse rescheduleAppointmentByCompany(UUID profileId, UUID appointmentId, RescheduleByCompanyRequest request);
}