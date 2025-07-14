package com.icar.platform.api.mapper.appointment;

import com.icar.platform.api.dto.response.appointment.AppointmentResponse;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.carwash.profile.AppointmentConfig;
import com.icar.platform.domain.repository.carwash.profile.AppointmentConfigRepository;
import com.icar.platform.domain.repository.carwash.profile.ReviewRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AppointmentMapper {

    @Autowired
    protected AppointmentConfigRepository appointmentConfigRepository;
    @Autowired
    protected ReviewRepository reviewRepository;
    public record ExtraServiceInfo(UUID id, String name, BigDecimal price, Integer time) {}
    @Mapping(target = "carWashName", source = "entity.profile.name")
    @Mapping(target = "carwashId", source = "entity.profile.id")
    @Mapping(source = "offering.id", target = "offeringId")
    @Mapping(source = "address.id", target = "addressId")
    @Mapping(target = "carWashPhone", source = "entity.profile.carWashRegistration.phone")
    @Mapping(target = "serviceName", source = "entity.offering.name")
    @Mapping(target = "extraServices", source = "entity", qualifiedByName = "mapExtraServices")
    @Mapping(target = "vehicleType", source = "entity.carType")
    @Mapping(target = "finalPrice", source = "entity.amountPaid")
    @Mapping(target = "addressStreet", source = "entity.address.street")
    @Mapping(target = "addressNumber", source = "entity.address.streetNumber")
    @Mapping(target = "addressCityState", expression = "java(entity.getAddress().getCity() + \"/\" + entity.getAddress().getState())")
    @Mapping(target = "addressInstructions", source = "entity.address.additionalInstructions")
    @Mapping(target = "estimatedTime", source = "entity.totalDurationMinutes")
    @Mapping(target = "minCancelNoticeMinutes", source = "entity", qualifiedByName = "getMinCancelNotice")
    @Mapping(target = "minEditNoticeMinutes", source = "entity", qualifiedByName = "getMinEditNotice")
    @Mapping(target = "hasBeenReviewed", source = "entity", qualifiedByName = "checkIfReviewed")
    public abstract AppointmentResponse toResponse(CarWashAppointment entity);

    @Named("getMinCancelNotice")
    public Integer getMinCancelNotice(CarWashAppointment appointment) {
        if (appointment == null || appointment.getProfile() == null) {
            return null;
        }
        return appointmentConfigRepository.findByProfile_Id(appointment.getProfile().getId())
                .map(AppointmentConfig::getMinCancelNoticeMinutes)
                .orElse(null);
    }

    @Named("getMinEditNotice")
    public Integer getMinEditNotice(CarWashAppointment appointment) {
        if (appointment == null || appointment.getProfile() == null) {
            return null;
        }
        return appointmentConfigRepository.findByProfile_Id(appointment.getProfile().getId())
                .map(AppointmentConfig::getMinEditNoticeMinutes)
                .orElse(null);
    }

    @Named("checkIfReviewed")
    public boolean checkIfReviewed(CarWashAppointment appointment) {
        if (appointment == null || appointment.getId() == null) {
            return false;
        }
        return reviewRepository.existsByAppointmentId(appointment.getId());
    }

    @Named("mapExtraServices")
    public List<AppointmentResponse.ExtraServiceInfo> mapExtraServices(CarWashAppointment appointment) {
        if (appointment.getSelectedExtraServices() == null || appointment.getCarType() == null) {
            return Collections.emptyList();
        }
        String vehicleType = appointment.getCarType();
        return appointment.getSelectedExtraServices().stream()
                .map(extra -> new AppointmentResponse.ExtraServiceInfo(
                        extra.getId(),
                        extra.getName(),
                        extra.getVehiclePrices().get(vehicleType),
                        extra.getVehicleEstimatedTimes().get(vehicleType)
                ))
                .collect(Collectors.toList());
    }
}