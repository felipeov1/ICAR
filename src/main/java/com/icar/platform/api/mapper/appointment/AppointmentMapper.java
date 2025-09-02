package com.icar.platform.api.mapper.appointment;

import com.icar.platform.api.dto.response.appointment.AppointmentResponse;
import com.icar.platform.api.dto.response.carwash.CompanyAppointmentResponse;
import com.icar.platform.domain.enums.PaymentMethod;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.carwash.offering.VehicleOfferingDetail;
import com.icar.platform.domain.model.carwash.profile.AppointmentConfig;
import com.icar.platform.domain.model.carwash.profile.CompanyCustomer;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.repository.carwash.profile.AppointmentConfigRepository;
import com.icar.platform.domain.repository.carwash.profile.ReviewRepository;
import org.mapstruct.*;
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

    @Mapping(target = "carWashName", source = "entity.profile.name")
    @Mapping(target = "carwashId", source = "entity.profile.id")
    @Mapping(target = "addressId", source = "address.id")
    @Mapping(target = "carWashPhone", source = "entity.profile.carWashRegistration.phone")
    @Mapping(target = "vehicleType", source = "entity.carType")
    @Mapping(target = "addressStreet", source = "entity.address.street")
    @Mapping(target = "addressNumber", source = "entity.address.streetNumber")
    @Mapping(target = "addressCityState", expression = "java(entity.getAddress().getCity() + \"/\" + entity.getAddress().getState())")
    @Mapping(target = "addressInstructions", source = "entity.address.additionalInstructions")
    @Mapping(target = "totalDurationMinutes", source = "entity.totalDurationMinutes")
    @Mapping(target = "minCancelNoticeMinutes", source = "entity", qualifiedByName = "getMinCancelNotice")
    @Mapping(target = "minEditNoticeMinutes", source = "entity", qualifiedByName = "getMinEditNotice")
    @Mapping(target = "hasBeenReviewed", source = "entity", qualifiedByName = "checkIfReviewed")
    @Mapping(target = "services", source = "entity", qualifiedByName = "mapServices")
    @Mapping(target = "originalPrice", source = "entity", qualifiedByName = "mapOriginalPrice")
    @Mapping(target = "discountAmount", source = "entity", qualifiedByName = "mapDiscountAmount")
    @Mapping(target = "finalPrice", source = "entity", qualifiedByName = "mapFinalPrice")
    public abstract AppointmentResponse toResponse(CarWashAppointment entity);


    @Mapping(target = "status", expression = "java(entity.getStatus().name())")
    @Mapping(target = "paymentMethod", expression = "java(entity.getPaymentMethod().name())")
    @Mapping(target = "vehicleType", source = "carType")
    @Mapping(target = "services", source = "entity", qualifiedByName = "mapCompanyServices")
    @Mapping(target = "addressStreet", source = "entity.address.street")
    @Mapping(target = "addressNumber", source = "entity.address.streetNumber")
    @Mapping(target = "addressCityState", expression = "java(entity.getAddress().getCity() + \"/\" + entity.getAddress().getState())")
    @Mapping(target = "originalPrice", source = "entity", qualifiedByName = "mapOriginalPrice")
    @Mapping(target = "discountAmount", source = "entity", qualifiedByName = "mapDiscountAmount")
    @Mapping(target = "finalPrice", source = "entity", qualifiedByName = "mapFinalPrice")
    public abstract CompanyAppointmentResponse toCompanyResponse(CarWashAppointment entity);

    @Named("mapOriginalPrice")
    public BigDecimal mapOriginalPrice(CarWashAppointment appointment) {
        if (appointment.getAppliedCoupons() != null && !appointment.getAppliedCoupons().isEmpty()) {
            return appointment.getAppliedCoupons().getFirst().getOriginalAmount();
        }
        return appointment.getOriginalAmount();
    }

    @Named("mapDiscountAmount")
    public BigDecimal mapDiscountAmount(CarWashAppointment appointment) {
        if (appointment.getAppliedCoupons() != null && !appointment.getAppliedCoupons().isEmpty()) {
            return appointment.getAppliedCoupons().iterator().next().getDiscountApplied();
        }
        return BigDecimal.ZERO;
    }

    @Named("mapConvenienceFee")
    public BigDecimal mapConvenienceFee(CarWashAppointment appointment) {
        if (appointment.getPaymentMethod() != PaymentMethod.PLATFORM || appointment.getAmountPaid() == null || appointment.getAmountPaid().compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal finalPrice = appointment.getAmountPaid();
        BigDecimal originalPrice = appointment.getOriginalAmount();
        BigDecimal discountAmount = mapDiscountAmount(appointment);

        BigDecimal priceBeforeFee = originalPrice.subtract(discountAmount);
        BigDecimal convenienceFee = finalPrice.subtract(priceBeforeFee);

        return convenienceFee.max(BigDecimal.ZERO);
    }

    @Named("mapFinalPrice")
    public BigDecimal mapFinalPrice(CarWashAppointment appointment) {
        BigDecimal amountPaid = appointment.getAmountPaid();

        if (amountPaid == null || amountPaid.compareTo(BigDecimal.ZERO) <= 0) {
            if (appointment.getPaymentMethod() == PaymentMethod.ON_SITE) {
                return appointment.getOriginalAmount().subtract(mapDiscountAmount(appointment));
            }

            return appointment.getOriginalAmount().subtract(mapDiscountAmount(appointment));
        }

        return amountPaid;
    }


    @Named("mapCompanyServices")
    public List<CompanyAppointmentResponse.ServiceInfo> mapCompanyServices(CarWashAppointment appointment) {
        if (appointment.getSelectedServices() == null || appointment.getCarType() == null) {
            return Collections.emptyList();
        }
        String vehicleType = appointment.getCarType();
        return appointment.getSelectedServices().stream()
                .map(service -> {
                    VehicleOfferingDetail detail = service.getVehicleDetails().get(vehicleType);
                    BigDecimal price = (detail != null) ? detail.getPrice() : null;
                    Integer time = (detail != null) ? detail.getEstimatedTime() : null;
                    return new CompanyAppointmentResponse.ServiceInfo(
                            service.getId(),
                            service.getName(),
                            price,
                            time
                    );
                })
                .collect(Collectors.toList());
    }

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

    @Named("mapServices")
    public List<AppointmentResponse.ServiceInfo> mapServices(CarWashAppointment appointment) {
        if (appointment.getSelectedServices() == null || appointment.getCarType() == null) {
            return Collections.emptyList();
        }
        String vehicleType = appointment.getCarType();
        return appointment.getSelectedServices().stream()
                .map(service -> {
                    VehicleOfferingDetail detail = service.getVehicleDetails().get(vehicleType);
                    BigDecimal price = (detail != null) ? detail.getPrice() : null;
                    Integer time = (detail != null) ? detail.getEstimatedTime() : null;
                    return new AppointmentResponse.ServiceInfo(
                            service.getId(),
                            service.getName(),
                            price,
                            time,
                            service.getServiceType()
                    );
                })
                .collect(Collectors.toList());
    }

    @AfterMapping
    protected void mapCustomerInfoToCompanyResponse(CarWashAppointment entity, @MappingTarget CompanyAppointmentResponse response) {
        if (entity == null) {
            return;
        }

        if (entity.getCompanyCustomer() != null) {
            CompanyCustomer companyCustomer = entity.getCompanyCustomer();
            response.setCustomer(new CompanyAppointmentResponse.CustomerInfo(
                    companyCustomer.getId(),
                    companyCustomer.getFullName(),
                    companyCustomer.getPhone()
            ));
        }
        else if (entity.getCustomer() != null) {
            Customer customer = entity.getCustomer();
            response.setCustomer(new CompanyAppointmentResponse.CustomerInfo(
                    customer.getId(),
                    customer.getFullName(),
                    customer.getPhone()
            ));
        }
    }

    @AfterMapping
    protected void mapCustomerInfoToResponse(CarWashAppointment entity, @MappingTarget AppointmentResponse response) {
        if (entity == null) {
            return;
        }

        if (entity.getCompanyCustomer() != null) {
            CompanyCustomer companyCustomer = entity.getCompanyCustomer();
            response.setCustomer(new AppointmentResponse.CustomerInfo(
                    companyCustomer.getId(),
                    companyCustomer.getFullName(),
                    companyCustomer.getPhone()
            ));
        }
        else if (entity.getCustomer() != null) {
            Customer customer = entity.getCustomer();
            response.setCustomer(new AppointmentResponse.CustomerInfo(
                    customer.getId(),
                    customer.getFullName(),
                    customer.getPhone()
            ));
        }
    }
}