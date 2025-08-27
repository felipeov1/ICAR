package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.request.carwash.profile.SpecialDayRequest;
import com.icar.platform.api.dto.request.carwash.profile.WeeklyScheduleRequest;
import com.icar.platform.api.dto.response.carwash.profile.SpecialDayResponse;
import com.icar.platform.api.dto.response.carwash.profile.WeeklyScheduleResponse;
import com.icar.platform.domain.model.carwash.profile.SpecialDay;
import com.icar.platform.domain.model.carwash.profile.WeeklySchedule;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-27T17:43:59+0000",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ScheduleMapperImpl implements ScheduleMapper {

    @Override
    public WeeklySchedule toEntity(WeeklyScheduleRequest request) {
        if ( request == null ) {
            return null;
        }

        WeeklySchedule weeklySchedule = new WeeklySchedule();

        weeklySchedule.setDayOfWeek( request.getDayOfWeek() );
        weeklySchedule.setStartTime( map( request.getStartTime() ) );
        weeklySchedule.setEndTime( map( request.getEndTime() ) );
        weeklySchedule.setAvailable( request.isAvailable() );
        weeklySchedule.setAppointmentIntervalMinutes( request.getAppointmentIntervalMinutes() );

        return weeklySchedule;
    }

    @Override
    public WeeklyScheduleResponse toResponse(WeeklySchedule entity) {
        if ( entity == null ) {
            return null;
        }

        UUID id = null;
        DayOfWeek dayOfWeek = null;
        String startTime = null;
        String endTime = null;
        boolean available = false;
        int appointmentIntervalMinutes = 0;

        id = entity.getId();
        dayOfWeek = entity.getDayOfWeek();
        startTime = map( entity.getStartTime() );
        endTime = map( entity.getEndTime() );
        available = entity.isAvailable();
        if ( entity.getAppointmentIntervalMinutes() != null ) {
            appointmentIntervalMinutes = entity.getAppointmentIntervalMinutes();
        }

        WeeklyScheduleResponse weeklyScheduleResponse = new WeeklyScheduleResponse( id, dayOfWeek, startTime, endTime, available, appointmentIntervalMinutes );

        return weeklyScheduleResponse;
    }

    @Override
    public void updateWeeklyScheduleFromRequest(WeeklyScheduleRequest request, WeeklySchedule entity) {
        if ( request == null ) {
            return;
        }

        entity.setDayOfWeek( request.getDayOfWeek() );
        entity.setStartTime( map( request.getStartTime() ) );
        entity.setEndTime( map( request.getEndTime() ) );
        entity.setAvailable( request.isAvailable() );
        entity.setAppointmentIntervalMinutes( request.getAppointmentIntervalMinutes() );
    }

    @Override
    public SpecialDay toEntity(SpecialDayRequest request) {
        if ( request == null ) {
            return null;
        }

        SpecialDay specialDay = new SpecialDay();

        specialDay.setStartDate( request.getStartDate() );
        specialDay.setEndDate( request.getEndDate() );
        specialDay.setDescription( request.getDescription() );
        specialDay.setStartTime( request.getStartTime() );
        specialDay.setEndTime( request.getEndTime() );

        return specialDay;
    }

    @Override
    public SpecialDayResponse toResponse(SpecialDay entity) {
        if ( entity == null ) {
            return null;
        }

        UUID id = null;
        LocalDate startDate = null;
        LocalDate endDate = null;
        String description = null;
        LocalTime startTime = null;
        LocalTime endTime = null;

        id = entity.getId();
        startDate = entity.getStartDate();
        endDate = entity.getEndDate();
        description = entity.getDescription();
        startTime = entity.getStartTime();
        endTime = entity.getEndTime();

        boolean isClosed = false;

        SpecialDayResponse specialDayResponse = new SpecialDayResponse( id, startDate, endDate, description, isClosed, startTime, endTime );

        return specialDayResponse;
    }

    @Override
    public void updateSpecialDayFromRequest(SpecialDayRequest request, SpecialDay entity) {
        if ( request == null ) {
            return;
        }

        entity.setStartDate( request.getStartDate() );
        entity.setEndDate( request.getEndDate() );
        entity.setDescription( request.getDescription() );
        entity.setStartTime( request.getStartTime() );
        entity.setEndTime( request.getEndTime() );
    }
}
