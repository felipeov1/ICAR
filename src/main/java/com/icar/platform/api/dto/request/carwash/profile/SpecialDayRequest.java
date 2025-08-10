package com.icar.platform.api.dto.request.carwash.profile;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class SpecialDayRequest {
    @NotNull(message = "A data de início é obrigatória.")
    @FutureOrPresent(message = "A data de início não pode ser no passado.")
    private LocalDate startDate;

    @NotNull(message = "A data de fim é obrigatória.")
    private LocalDate endDate;

    private String description;

    @NotNull
    private Boolean isClosed;

    private LocalTime startTime;
    private LocalTime endTime;

    @AssertTrue(message = "A data de fim não pode ser anterior à data de início.")
    private boolean isDateRangeValid() {
        return startDate == null || endDate == null || !endDate.isBefore(startDate);
    }

    @AssertTrue(message = "Se 'isClosed' for falso, os horários de início e fim são obrigatórios.")
    private boolean areTimesProvidedIfNotClosed() {
        if (isClosed != null && !isClosed) {
            return startTime != null && endTime != null;
        }
        return true;
    }

    @AssertTrue(message = "A hora de início deve ser anterior à hora de fim.")
    private boolean isTimeRangeValid() {
        if (startTime != null && endTime != null) {
            return !endTime.isBefore(startTime);
        }
        return true;
    }
}