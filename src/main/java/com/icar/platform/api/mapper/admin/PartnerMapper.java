package com.icar.platform.api.mapper.admin;

import com.icar.platform.api.dto.response.admin.PartnerResponse;
import com.icar.platform.domain.model.admin.Subscription;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.platform.domain.enums.SubscriptionStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.util.Comparator;

@Mapper(componentModel = "spring")
public interface PartnerMapper {

    @Mapping(target = "status", expression = "java(calculatePartnerStatus(partner))")
    @Mapping(target = "joinDate", source = "createdAt", qualifiedByName = "toLocalDate")
    @Mapping(target = "currentPlan", expression = "java(mapCurrentPlan(partner))")
    PartnerResponse toPartnerResponse(CarWashRegistration partner);

    default String calculatePartnerStatus(CarWashRegistration partner) {
        if (partner.getDeletedAt() != null) {
            return "INACTIVE";
        }
        return partner.getSubscriptions().stream()
                .filter(s -> s.getStatus() == SubscriptionStatus.ACTIVE)
                .findFirst()
                .map(sub -> sub.getPlan().getDurationInDays() != null ? "TRIAL" : "ACTIVE")
                .orElse("INACTIVE");
    }

    default PartnerResponse.CurrentPlanDto mapCurrentPlan(CarWashRegistration partner) {
        return partner.getSubscriptions().stream()
                .max(Comparator.comparing(Subscription::getStartDate))
                .map(sub -> new PartnerResponse.CurrentPlanDto(
                        sub.getPlan().getName(),
                        sub.getEndDate() != null ? sub.getEndDate().toLocalDate() : null
                ))
                .orElse(null);
    }

    @Named("toLocalDate")
    default LocalDate toLocalDate(java.time.LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate() : null;
    }
}