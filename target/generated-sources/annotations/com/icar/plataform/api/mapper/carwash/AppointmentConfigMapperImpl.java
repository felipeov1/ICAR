package com.icar.plataform.api.mapper.carwash;

import com.icar.plataform.api.dto.request.carwash.profile.AppointmentConfigRequest;
import com.icar.plataform.api.dto.response.carwash.profile.AppointmentConfigResponse;
import com.icar.plataform.domain.model.carwash.profile.AppointmentConfig;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T19:41:33-0300",
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

        return appointmentConfigResponse;
    }

    @Override
    public void updateFromRequest(AppointmentConfigRequest request, AppointmentConfig entity) {
        if ( request == null ) {
            return;
        }

        entity.setMinAdvanceNoticeMinutes( request.getMinAdvanceNoticeMinutes() );
        entity.setMinEditNoticeMinutes( request.getMinEditNoticeMinutes() );
        entity.setMinCancelNoticeMinutes( request.getMinCancelNoticeMinutes() );
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

        return appointmentConfig;
    }
}
