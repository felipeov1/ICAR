package com.icar.platform.api.mapper.appointment;

import com.icar.platform.api.dto.response.admin.DashboardStatsResponse;
import com.icar.platform.api.dto.response.appointment.AppointmentResponse;
import com.icar.platform.api.dto.response.carwash.CompanyAppointmentResponse;
import com.icar.platform.domain.enums.PaymentMethod;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.carwash.offering.VehicleOfferingDetail;
import com.icar.platform.domain.model.carwash.profile.AppointmentConfig;
import com.icar.platform.domain.model.carwash.profile.CompanyCustomer;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.model.customer.CustomerAddress;
import com.icar.platform.domain.repository.carwash.profile.AppointmentConfigRepository;
import com.icar.platform.domain.repository.carwash.profile.ReviewRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AppointmentMapper {

    @Autowired
    protected AppointmentConfigRepository appointmentConfigRepository;
    @Autowired
    protected ReviewRepository reviewRepository;

    @Mapping(target = "carWashName", source = "entity.profile.name")
    @Mapping(target = "carwashId", source = "entity.profile.id")
    @Mapping(target = "carWashPhone", source = "entity.profile.carWashRegistration.phone")
    @Mapping(target = "vehicleType", source = "entity.carType")
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
    @Mapping(target = "originalPrice", source = "entity", qualifiedByName = "mapOriginalPrice")
    @Mapping(target = "discountAmount", source = "entity", qualifiedByName = "mapDiscountAmount")
    @Mapping(target = "finalPrice", source = "entity", qualifiedByName = "mapFinalPrice")
    public abstract CompanyAppointmentResponse toCompanyResponse(CarWashAppointment entity);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "customerName", source = "entity", qualifiedByName = "mapCustomerName")
    @Mapping(target = "contactInfo", source = "entity", qualifiedByName = "mapContactInfo")
    @Mapping(target = "partnerName", source = "profile.name")
    @Mapping(target = "paymentStatus", source = "paymentMethod")
    @Mapping(target = "date", source = "dateTime")
    @Mapping(target = "creationChannel", source = "creationChannel")
    public abstract DashboardStatsResponse.DetailItemDto toDetailItemDto(CarWashAppointment entity);

    @Named("mapCustomerName")
    String mapCustomerName(CarWashAppointment appointment) {
        if (appointment.getCompanyCustomer() != null) {
            return appointment.getCompanyCustomer().getFullName();
        }
        if (appointment.getCustomer() != null) {
            return appointment.getCustomer().getFullName();
        }
        return "N/A";
    }

    @Named("mapContactInfo")
    String mapContactInfo(CarWashAppointment appointment) {
        if (appointment.getCompanyCustomer() != null) {
            return appointment.getCompanyCustomer().getPhone();
        }
        if (appointment.getCustomer() != null) {
            return appointment.getCustomer().getPhone();
        }
        return "N/A";
    }

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
            return appointment.getAppliedCoupons().getFirst().getDiscountApplied();
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
    protected void mapAddressInfoToResponse(CarWashAppointment entity, @MappingTarget AppointmentResponse response) {
        CustomerAddress address = entity.getAddress();
        if (address != null) {
            response.setAddressId(address.getId());
            response.setAddressStreet(address.getStreet());
            response.setAddressName(address.getAddressName());
            response.setAddressNeighborhood(address.getNeighborhood());
            response.setAddressNumber(address.getStreetNumber());
            response.setAddressCityState(address.getCity() + "/" + address.getState());
            response.setAddressInstructions(address.getAdditionalInstructions());
        }
    }

    @AfterMapping
    protected void mapAddressInfoToCompanyResponse(CarWashAppointment entity, @MappingTarget CompanyAppointmentResponse response) {
        CustomerAddress address = entity.getAddress();
        if (address != null) {
            response.setAddressName(address.getAddressName());
            response.setAddressNeighborhood(address.getNeighborhood());
            response.setAddressInstructions(address.getAdditionalInstructions());
            response.setAddressStreet(address.getStreet());
            response.setAddressNumber(address.getStreetNumber());
            response.setAddressCityState(address.getCity() + "/" + address.getState());
        }
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