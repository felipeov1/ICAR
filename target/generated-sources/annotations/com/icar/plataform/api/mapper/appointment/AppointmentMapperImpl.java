package com.icar.plataform.api.mapper.appointment;

import com.icar.plataform.api.dto.request.appointment.AppointmentRequest;
import com.icar.plataform.api.dto.response.appointment.AddressSummaryResponse;
import com.icar.plataform.api.dto.response.appointment.AppointmentResponse;
import com.icar.plataform.domain.enums.AppointmentStatus;
import com.icar.plataform.domain.enums.PaymentMethod;
import com.icar.plataform.domain.model.appointment.CarWashAppointment;
import com.icar.plataform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.plataform.domain.model.carwash.offering.CarWashOffering;
import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import com.icar.plataform.domain.model.customer.Customer;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T19:41:33-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class AppointmentMapperImpl implements AppointmentMapper {

    @Override
    public CarWashAppointment toEntity(AppointmentRequest dto) {
        if ( dto == null ) {
            return null;
        }

        CarWashAppointment carWashAppointment = new CarWashAppointment();

        carWashAppointment.setProfile( mapProfile( dto.carWashId() ) );
        carWashAppointment.setOffering( mapOffering( dto.offeringId() ) );
        carWashAppointment.setAddress( mapAddress( dto.addressId() ) );
        carWashAppointment.setCarType( dto.carType() );
        carWashAppointment.setDateTime( dto.dateTime() );
        if ( dto.paymentMethod() != null ) {
            carWashAppointment.setPaymentMethod( Enum.valueOf( PaymentMethod.class, dto.paymentMethod() ) );
        }

        carWashAppointment.setStatus( AppointmentStatus.CONFIRMED );
        carWashAppointment.setCreatedAt( java.time.LocalDateTime.now() );
        carWashAppointment.setUpdatedAt( java.time.LocalDateTime.now() );

        return carWashAppointment;
    }

    @Override
    public AppointmentResponse toDto(CarWashAppointment entity) {
        if ( entity == null ) {
            return null;
        }

        UUID customerId = null;
        String customerName = null;
        UUID carWashId = null;
        String carWashName = null;
        AddressSummaryResponse address = null;
        String carType = null;
        UUID offeringId = null;
        String serviceName = null;
        UUID id = null;
        LocalDateTime dateTime = null;
        AppointmentStatus status = null;
        PaymentMethod paymentMethod = null;
        BigDecimal amountPaid = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        customerId = entityCustomerId( entity );
        customerName = entityCustomerFullName( entity );
        carWashId = entityProfileCarWashRegistrationId( entity );
        carWashName = entityProfileCarWashRegistrationTradeName( entity );
        address = toAddressSummary( entity.getAddress() );
        carType = entity.getCarType();
        offeringId = entityOfferingId( entity );
        serviceName = entityOfferingName( entity );
        id = entity.getId();
        dateTime = entity.getDateTime();
        status = entity.getStatus();
        paymentMethod = entity.getPaymentMethod();
        amountPaid = entity.getAmountPaid();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        AppointmentResponse appointmentResponse = new AppointmentResponse( id, customerId, customerName, carWashId, carWashName, offeringId, serviceName, address, carType, dateTime, status, paymentMethod, amountPaid, createdAt, updatedAt );

        return appointmentResponse;
    }

    private UUID entityCustomerId(CarWashAppointment carWashAppointment) {
        Customer customer = carWashAppointment.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getId();
    }

    private String entityCustomerFullName(CarWashAppointment carWashAppointment) {
        Customer customer = carWashAppointment.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getFullName();
    }

    private UUID entityProfileCarWashRegistrationId(CarWashAppointment carWashAppointment) {
        CarWashProfile profile = carWashAppointment.getProfile();
        if ( profile == null ) {
            return null;
        }
        CarWashRegistration carWashRegistration = profile.getCarWashRegistration();
        if ( carWashRegistration == null ) {
            return null;
        }
        return carWashRegistration.getId();
    }

    private String entityProfileCarWashRegistrationTradeName(CarWashAppointment carWashAppointment) {
        CarWashProfile profile = carWashAppointment.getProfile();
        if ( profile == null ) {
            return null;
        }
        CarWashRegistration carWashRegistration = profile.getCarWashRegistration();
        if ( carWashRegistration == null ) {
            return null;
        }
        return carWashRegistration.getTradeName();
    }

    private UUID entityOfferingId(CarWashAppointment carWashAppointment) {
        CarWashOffering offering = carWashAppointment.getOffering();
        if ( offering == null ) {
            return null;
        }
        return offering.getId();
    }

    private String entityOfferingName(CarWashAppointment carWashAppointment) {
        CarWashOffering offering = carWashAppointment.getOffering();
        if ( offering == null ) {
            return null;
        }
        return offering.getName();
    }
}
