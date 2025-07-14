package com.icar.platform.api.mapper.appointment;

import com.icar.platform.api.dto.response.appointment.AppointmentResponse;
import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.platform.domain.model.carwash.offering.CarWashOffering;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.model.customer.CustomerAddress;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-14T19:39:16-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class AppointmentMapperImpl extends AppointmentMapper {

    @Override
    public AppointmentResponse toResponse(CarWashAppointment entity) {
        if ( entity == null ) {
            return null;
        }

        String carWashName = null;
        UUID carwashId = null;
        UUID offeringId = null;
        UUID addressId = null;
        String carWashPhone = null;
        String serviceName = null;
        List<AppointmentResponse.ExtraServiceInfo> extraServices = null;
        String vehicleType = null;
        BigDecimal finalPrice = null;
        String addressStreet = null;
        String addressNumber = null;
        String addressInstructions = null;
        int estimatedTime = 0;
        Integer minCancelNoticeMinutes = null;
        Integer minEditNoticeMinutes = null;
        boolean hasBeenReviewed = false;
        UUID id = null;
        ZonedDateTime dateTime = null;
        AppointmentStatus status = null;
        String paymentMethod = null;

        carWashName = entityProfileName( entity );
        carwashId = entityProfileId( entity );
        offeringId = entityOfferingId( entity );
        addressId = entityAddressId( entity );
        carWashPhone = entityProfileCarWashRegistrationPhone( entity );
        serviceName = entityOfferingName( entity );
        extraServices = mapExtraServices( entity );
        vehicleType = entity.getCarType();
        finalPrice = entity.getAmountPaid();
        addressStreet = entityAddressStreet( entity );
        addressNumber = entityAddressStreetNumber( entity );
        addressInstructions = entityAddressAdditionalInstructions( entity );
        if ( entity.getTotalDurationMinutes() != null ) {
            estimatedTime = entity.getTotalDurationMinutes();
        }
        minCancelNoticeMinutes = getMinCancelNotice( entity );
        minEditNoticeMinutes = getMinEditNotice( entity );
        hasBeenReviewed = checkIfReviewed( entity );
        id = entity.getId();
        dateTime = entity.getDateTime();
        status = entity.getStatus();
        if ( entity.getPaymentMethod() != null ) {
            paymentMethod = entity.getPaymentMethod().name();
        }

        String addressCityState = entity.getAddress().getCity() + "/" + entity.getAddress().getState();

        AppointmentResponse appointmentResponse = new AppointmentResponse( id, carwashId, offeringId, addressId, carWashName, carWashPhone, serviceName, extraServices, estimatedTime, dateTime, vehicleType, status, paymentMethod, finalPrice, addressStreet, addressNumber, addressCityState, addressInstructions, minCancelNoticeMinutes, minEditNoticeMinutes, hasBeenReviewed );

        return appointmentResponse;
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

    private UUID entityOfferingId(CarWashAppointment carWashAppointment) {
        CarWashOffering offering = carWashAppointment.getOffering();
        if ( offering == null ) {
            return null;
        }
        return offering.getId();
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

    private String entityOfferingName(CarWashAppointment carWashAppointment) {
        CarWashOffering offering = carWashAppointment.getOffering();
        if ( offering == null ) {
            return null;
        }
        return offering.getName();
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
}
