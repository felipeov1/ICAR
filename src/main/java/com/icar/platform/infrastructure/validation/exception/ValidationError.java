package com.icar.platform.infrastructure.validation.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ValidationError {
    private final String field;
    private final String message;
    private final String errorCode;
    @Builder.Default
    private final String errorType = "VALIDATION_ERROR";
}