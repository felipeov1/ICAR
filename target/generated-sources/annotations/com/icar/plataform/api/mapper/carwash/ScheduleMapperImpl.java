package com.icar.plataform.api.mapper.carwash;

import com.icar.plataform.api.dto.request.carwash.profile.SpecialDayRequest;
import com.icar.plataform.api.dto.request.carwash.profile.WeeklyScheduleRequest;
import com.icar.plataform.api.dto.response.carwash.profile.SpecialDayResponse;
import com.icar.plataform.api.dto.response.carwash.profile.WeeklyScheduleResponse;
import com.icar.plataform.domain.model.carwash.profile.SpecialDay;
import com.icar.plataform.domain.model.carwash.profile.WeeklySchedule;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T19:41:33-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
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

        specialDay.setDate( request.getDate() );
        specialDay.setStartTime( request.getStartTime() );
        specialDay.setEndTime( request.getEndTime() );

        return specialDay;
    }

    @Override
    public SpecialDayResponse toResponse(SpecialDay entity) {
        if ( entity == null ) {
            return null;
        }

        boolean isActive = false;
        UUID id = null;
        LocalDate date = null;
        LocalTime startTime = null;
        LocalTime endTime = null;

        isActive = entity.isActive();
        id = entity.getId();
        date = entity.getDate();
        startTime = entity.getStartTime();
        endTime = entity.getEndTime();

        SpecialDayResponse specialDayResponse = new SpecialDayResponse( id, date, startTime, endTime, isActive );

        return specialDayResponse;
    }

    @Override
    public void updateSpecialDayFromRequest(SpecialDayRequest request, SpecialDay entity) {
        if ( request == null ) {
            return;
        }

        entity.setDate( request.getDate() );
        entity.setStartTime( request.getStartTime() );
        entity.setEndTime( request.getEndTime() );
    }
}
