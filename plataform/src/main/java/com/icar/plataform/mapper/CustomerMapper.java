package com.icar.plataform.mapper;

import com.icar.plataform.domain.model.Appointment;
import com.icar.plataform.domain.model.Customer;
import com.icar.plataform.dto.request.CustomerRequest;
import com.icar.plataform.dto.response.AppointmentResponse;
import com.icar.plataform.dto.response.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    Customer toEntity(CustomerRequest dto);

    CustomerResponse toDto(Customer entity);

    @Mapping(target = "customerName", source = "customer.fullName")
    AppointmentResponse toAppointmentDto(Appointment appointment);
}