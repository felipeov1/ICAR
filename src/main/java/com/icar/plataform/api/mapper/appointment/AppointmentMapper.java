package com.icar.plataform.api.mapper.appointment;

import com.icar.plataform.api.dto.request.appointment.AppointmentRequest;
import com.icar.plataform.api.dto.response.appointment.AddressSummaryResponse;
import com.icar.plataform.api.dto.response.appointment.AppointmentResponse;
import com.icar.plataform.domain.model.appointment.CarWashAppointment;
import com.icar.plataform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.plataform.domain.model.carwash.offering.CarWashOffering;
import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import com.icar.plataform.domain.model.customer.Customer;
import com.icar.plataform.domain.model.customer.CustomerAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", source = "carWashId", qualifiedByName = "mapProfile")
    @Mapping(target = "offering", source = "offeringId", qualifiedByName = "mapOffering")
    @Mapping(target = "address", source = "addressId", qualifiedByName = "mapAddress")
    @Mapping(target = "carType", source = "carType")
    @Mapping(target = "status", constant = "CONFIRMED")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "deletedAt", ignore = true)
    CarWashAppointment toEntity(AppointmentRequest dto);

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.fullName")
    @Mapping(target = "carWashId", source = "profile.carWashRegistration.id")
    @Mapping(target = "carWashName", source = "profile.carWashRegistration.tradeName")
    @Mapping(target = "address", source = "address", qualifiedByName = "toAddressSummary")
    @Mapping(target = "carType", source = "carType")
    @Mapping(target = "offeringId", source = "offering.id")
    @Mapping(target = "serviceName", source = "offering.name")
    AppointmentResponse toDto(CarWashAppointment entity);


    @Named("toAddressSummary")
    default AddressSummaryResponse toAddressSummary(CustomerAddress address) {
        if (address == null) return null;

        return new AddressSummaryResponse(
                address.getId(),
                address.getStreet(),
                address.getStreetNumber(),
                address.getCity()
        );
    }


    // Mapear UUID para Customer com ID
    @Named("mapCustomer")
    default Customer mapCustomer(UUID customerId) {
        if (customerId == null) return null;
        Customer customer = new Customer();
        customer.setId(customerId);
        return customer;
    }

    // Mapear UUID para CarWashProfile com CarWashRegistration setado
    @Named("mapProfile")
    default CarWashProfile mapProfile(UUID carWashId) {
        if (carWashId == null) return null;
        CarWashRegistration registration = new CarWashRegistration();
        registration.setId(carWashId);

        CarWashProfile profile = new CarWashProfile();
        profile.setCarWashRegistration(registration);
        return profile;
    }

    // Mapear UUID para Offering com ID
    @Named("mapOffering")
    default CarWashOffering mapOffering(UUID offeringId) {
        if (offeringId == null) return null;
        CarWashOffering offering = new CarWashOffering();
        offering.setId(offeringId);
        return offering;
    }

    // Mapear UUID para Address com ID
    @Named("mapAddress")
    default CustomerAddress mapAddress(UUID addressId) {
        if (addressId == null) return null;
        CustomerAddress address = new CustomerAddress();
        address.setId(addressId);
        return address;
    }
}
