package com.icar.plataform.mapper;

import com.icar.plataform.domain.model.*;
import com.icar.plataform.dto.request.AppointmentRequest;
import com.icar.plataform.dto.response.AppointmentResponse;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class AppointmentMapper {

    @Autowired
    protected CustomerMapper customerMapper;

    @Autowired
    protected CarWashMapper carWashMapper;

    @Autowired
    protected ServiceMapper serviceMapper;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "customer", source = "customerId", qualifiedByName = "idToCustomer")
    @Mapping(target = "carWash", source = "carWashId", qualifiedByName = "idToCarWash")
    @Mapping(target = "service", source = "serviceId", qualifiedByName = "idToService")
    public abstract Appointment toEntity(AppointmentRequest dto);

    @Mapping(target = "customerName", source = "customer.fullName")
    @Mapping(target = "carWashName", source = "carWash.tradeName")
    @Mapping(target = "serviceName", source = "service.name")
    public abstract AppointmentResponse toDto(Appointment entity);

    @Named("idToCustomer")
    protected Customer idToCustomer(UUID id) {
        if (id == null) return null;
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }

    @Named("idToCarWash")
    protected CarWash idToCarWash(UUID id) {
        if (id == null) return null;
        CarWash carWash = new CarWash();
        carWash.setId(id);
        return carWash;
    }

    @Named("idToService")
    protected Service idToService(UUID id) {
        if (id == null) return null;
        Service service = new Service();
        service.setId(id);
        return service;
    }
}