package com.icar.platform.api.mapper.appointment;

import com.icar.platform.api.dto.response.appointment.AppointmentResponse;
import com.icar.platform.api.dto.response.carwash.CompanyAppointmentResponse;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.model.customer.CustomerAddress;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-01T20:38:40-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class AppointmentMapperImpl extends AppointmentMapper {

    @Override
    public AppointmentResponse toResponse(CarWashAppointment entity) {
        if ( entity == null ) {
            return null;
        }

        AppointmentResponse appointmentResponse = new AppointmentResponse();

        appointmentResponse.setCarWashName( entityProfileName( entity ) );
        appointmentResponse.setCarwashId( entityProfileId( entity ) );
        appointmentResponse.setAddressId( entityAddressId( entity ) );
        appointmentResponse.setCarWashPhone( entityProfileCarWashRegistrationPhone( entity ) );
        appointmentResponse.setVehicleType( entity.getCarType() );
        appointmentResponse.setAddressStreet( entityAddressStreet( entity ) );
        appointmentResponse.setAddressNumber( entityAddressStreetNumber( entity ) );
        appointmentResponse.setAddressInstructions( entityAddressAdditionalInstructions( entity ) );
        if ( entity.getTotalDurationMinutes() != null ) {
            appointmentResponse.setTotalDurationMinutes( entity.getTotalDurationMinutes() );
        }
        appointmentResponse.setMinCancelNoticeMinutes( getMinCancelNotice( entity ) );
        appointmentResponse.setMinEditNoticeMinutes( getMinEditNotice( entity ) );
        appointmentResponse.setHasBeenReviewed( checkIfReviewed( entity ) );
        appointmentResponse.setServices( mapServices( entity ) );
        appointmentResponse.setOriginalPrice( mapOriginalPrice( entity ) );
        appointmentResponse.setDiscountAmount( mapDiscountAmount( entity ) );
        appointmentResponse.setFinalPrice( mapFinalPrice( entity ) );
        appointmentResponse.setId( entity.getId() );
        appointmentResponse.setDateTime( entity.getDateTime() );
        appointmentResponse.setStatus( entity.getStatus() );
        if ( entity.getPaymentMethod() != null ) {
            appointmentResponse.setPaymentMethod( entity.getPaymentMethod().name() );
        }
        appointmentResponse.setCustomer( customerToCustomerInfo( entity.getCustomer() ) );

        appointmentResponse.setAddressCityState( entity.getAddress().getCity() + "/" + entity.getAddress().getState() );

        mapCustomerInfoToResponse( entity, appointmentResponse );

        return appointmentResponse;
    }

    @Override
    public CompanyAppointmentResponse toCompanyResponse(CarWashAppointment entity) {
        if ( entity == null ) {
            return null;
        }

        CompanyAppointmentResponse companyAppointmentResponse = new CompanyAppointmentResponse();

        companyAppointmentResponse.setVehicleType( entity.getCarType() );
        companyAppointmentResponse.setServices( mapCompanyServices( entity ) );
        companyAppointmentResponse.setAddressStreet( entityAddressStreet( entity ) );
        companyAppointmentResponse.setAddressNumber( entityAddressStreetNumber( entity ) );
        companyAppointmentResponse.setOriginalPrice( mapOriginalPrice( entity ) );
        companyAppointmentResponse.setDiscountAmount( mapDiscountAmount( entity ) );
        companyAppointmentResponse.setFinalPrice( mapFinalPrice( entity ) );
        companyAppointmentResponse.setId( entity.getId() );
        companyAppointmentResponse.setDateTime( entity.getDateTime() );
        if ( entity.getTotalDurationMinutes() != null ) {
            companyAppointmentResponse.setTotalDurationMinutes( entity.getTotalDurationMinutes() );
        }
        companyAppointmentResponse.setCustomer( customerToCustomerInfo1( entity.getCustomer() ) );

        companyAppointmentResponse.setStatus( entity.getStatus().name() );
        companyAppointmentResponse.setPaymentMethod( entity.getPaymentMethod().name() );
        companyAppointmentResponse.setAddressCityState( entity.getAddress().getCity() + "/" + entity.getAddress().getState() );

        mapCustomerInfoToCompanyResponse( entity, companyAppointmentResponse );

        return companyAppointmentResponse;
    }

    private String entityProfileName(CarWashAppointment carWashAppointment) {
        CarWashProfile profile = carWashAppointment.getProfile();
        if ( profile == null ) {
            return null;
        }
        return profile.getName();
    }

    private UUID entityProfileId(CarWashAppointment carWashAppointment) {
        CarWashProfile profile = carWashAppointment.getProfile();
        if ( profile == null ) {
            return null;
        }
        return profile.getId();
    }

    private UUID entityAddressId(CarWashAppointment carWashAppointment) {
        CustomerAddress address = carWashAppointment.getAddress();
        if ( address == null ) {
            return null;
        }
        return address.getId();
    }

    private String entityProfileCarWashRegistrationPhone(CarWashAppointment carWashAppointment) {
        CarWashProfile profile = carWashAppointment.getProfile();
        if ( profile == null ) {
            return null;
        }
        CarWashRegistration carWashRegistration = profile.getCarWashRegistration();
        if ( carWashRegistration == null ) {
            return null;
        }
        return carWashRegistration.getPhone();
    }

    private String entityAddressStreet(CarWashAppointment carWashAppointment) {
        CustomerAddress address = carWashAppointment.getAddress();
        if ( address == null ) {
            return null;
        }
        return address.getStreet();
    }

    private String entityAddressStreetNumber(CarWashAppointment carWashAppointment) {
        CustomerAddress address = carWashAppointment.getAddress();
        if ( address == null ) {
            return null;
        }
        return address.getStreetNumber();
    }

    private String entityAddressAdditionalInstructions(CarWashAppointment carWashAppointment) {
        CustomerAddress address = carWashAppointment.getAddress();
        if ( address == null ) {
            return null;
        }
        return address.getAdditionalInstructions();
    }

    protected AppointmentResponse.CustomerInfo customerToCustomerInfo(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        UUID id = null;
        String phone = null;

        id = customer.getId();
        phone = customer.getPhone();

        String name = null;

        AppointmentResponse.CustomerInfo customerInfo = new AppointmentResponse.CustomerInfo( id, name, phone );

        return customerInfo;
    }

    protected CompanyAppointmentResponse.CustomerInfo customerToCustomerInfo1(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CompanyAppointmentResponse.CustomerInfo customerInfo = new CompanyAppointmentResponse.CustomerInfo();

        customerInfo.setId( customer.getId() );
        customerInfo.setPhone( customer.getPhone() );

        return customerInfo;
    }
}
