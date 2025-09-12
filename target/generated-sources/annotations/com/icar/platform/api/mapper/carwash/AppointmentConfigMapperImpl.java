package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.request.carwash.profile.AppointmentConfigRequest;
import com.icar.platform.api.dto.response.carwash.profile.AppointmentConfigResponse;
import com.icar.platform.domain.model.carwash.profile.AppointmentConfig;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-11T09:51:48-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class AppointmentConfigMapperImpl implements AppointmentConfigMapper {

    @Override
    public AppointmentConfigResponse toResponse(AppointmentConfig entity) {
        if ( entity == null ) {
            return null;
        }

        AppointmentConfigResponse appointmentConfigResponse = new AppointmentConfigResponse();

        appointmentConfigResponse.setMinAdvanceNoticeMinutes( entity.getMinAdvanceNoticeMinutes() );
        appointmentConfigResponse.setMinEditNoticeMinutes( entity.getMinEditNoticeMinutes() );
        appointmentConfigResponse.setMinCancelNoticeMinutes( entity.getMinCancelNoticeMinutes() );
        appointmentConfigResponse.setMaxAdvanceBookingDays( entity.getMaxAdvanceBookingDays() );
        appointmentConfigResponse.setGapMinutes( entity.getGapMinutes() );
        appointmentConfigResponse.setAllowOvertime( entity.isAllowOvertime() );

        return appointmentConfigResponse;
    }

    @Override
    public void updateFromRequest(AppointmentConfigRequest request, AppointmentConfig entity) {
        if ( request == null ) {
            return;
        }

        if ( request.getMinAdvanceNoticeMinutes() != null ) {
            entity.setMinAdvanceNoticeMinutes( request.getMinAdvanceNoticeMinutes() );
        }
        if ( request.getMaxAdvanceBookingDays() != null ) {
            entity.setMaxAdvanceBookingDays( request.getMaxAdvanceBookingDays() );
        }
        if ( request.getMinEditNoticeMinutes() != null ) {
            entity.setMinEditNoticeMinutes( request.getMinEditNoticeMinutes() );
        }
        if ( request.getMinCancelNoticeMinutes() != null ) {
            entity.setMinCancelNoticeMinutes( request.getMinCancelNoticeMinutes() );
        }
        if ( request.getGapMinutes() != null ) {
            entity.setGapMinutes( request.getGapMinutes() );
        }
        if ( request.getAllowOvertime() != null ) {
            entity.setAllowOvertime( request.getAllowOvertime() );
        }
    }

    @Override
    public AppointmentConfig toEntity(AppointmentConfigRequest request) {
        if ( request == null ) {
            return null;
        }

        AppointmentConfig appointmentConfig = new AppointmentConfig();

        appointmentConfig.setMinAdvanceNoticeMinutes( request.getMinAdvanceNoticeMinutes() );
        appointmentConfig.setMinEditNoticeMinutes( request.getMinEditNoticeMinutes() );
        appointmentConfig.setMinCancelNoticeMinutes( request.getMinCancelNoticeMinutes() );
        appointmentConfig.setMaxAdvanceBookingDays( request.getMaxAdvanceBookingDays() );
        appointmentConfig.setGapMinutes( request.getGapMinutes() );
        if ( request.getAllowOvertime() != null ) {
            appointmentConfig.setAllowOvertime( request.getAllowOvertime() );
        }

        return appointmentConfig;
    }
}
