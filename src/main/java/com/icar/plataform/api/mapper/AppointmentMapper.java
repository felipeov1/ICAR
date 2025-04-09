package com.icar.plataform.api.mapper;

import com.icar.plataform.domain.model.*;
import com.icar.plataform.api.dto.request.AppointmentRequest;
import com.icar.plataform.api.dto.response.AppointmentResponse;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        uses = {CustomerMapper.class, CarWashMapper.class, ServiceMapper.class}
)
public interface AppointmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "customer", source = "customerId")
    @Mapping(target = "carWash", source = "carWashId")
    @Mapping(target = "service", source = "serviceId")
    Appointment toEntity(AppointmentRequest dto);

    @Mapping(target = "customerName", source = "customer.fullName")
    @Mapping(target = "carWashName", source = "carWash.tradeName")
    @Mapping(target = "serviceName", source = "service.name")
    AppointmentResponse toDto(Appointment entity);

    default Customer idToCustomer(UUID id) {
        if (id == null) return null;
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }

    default CarWash idToCarWash(UUID id) {
        if (id == null) return null;
        CarWash carWash = new CarWash();
        carWash.setId(id);
        return carWash;
    }

    default Service idToService(UUID id) {
        if (id == null) return null;
        Service service = new Service();
        service.setId(id);
        return service;
    }
}