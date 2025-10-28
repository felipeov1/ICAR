package com.icar.platform.api.mapper.admin;

import com.icar.platform.api.dto.request.admin.PlanRequest;
import com.icar.platform.api.dto.response.admin.PlanResponse;
import com.icar.platform.domain.model.admin.Plan;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-24T17:13:50-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class PlanMapperImpl implements PlanMapper {

    @Override
    public Plan toEntity(PlanRequest request) {
        if ( request == null ) {
            return null;
        }

        Plan plan = new Plan();

        plan.setName( request.name() );
        plan.setPrice( request.price() );
        plan.setBillingFrequency( request.billingFrequency() );
        plan.setBillingPeriod( request.billingPeriod() );
        plan.setDurationInDays( request.durationInDays() );

        return plan;
    }

    @Override
    public PlanResponse toResponse(Plan plan) {
        if ( plan == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        BigDecimal price = null;
        int billingFrequency = 0;
        ChronoUnit billingPeriod = null;
        Integer durationInDays = null;
        LocalDateTime createdAt = null;

        id = plan.getId();
        name = plan.getName();
        price = plan.getPrice();
        billingFrequency = plan.getBillingFrequency();
        billingPeriod = plan.getBillingPeriod();
        durationInDays = plan.getDurationInDays();
        createdAt = plan.getCreatedAt();

        PlanResponse planResponse = new PlanResponse( id, name, price, billingFrequency, billingPeriod, durationInDays, createdAt );

        return planResponse;
    }

    @Override
    public void updateEntityFromRequest(PlanRequest request, Plan plan) {
        if ( request == null ) {
            return;
        }

        plan.setName( request.name() );
        plan.setPrice( request.price() );
        plan.setBillingFrequency( request.billingFrequency() );
        plan.setBillingPeriod( request.billingPeriod() );
        plan.setDurationInDays( request.durationInDays() );
    }
}
