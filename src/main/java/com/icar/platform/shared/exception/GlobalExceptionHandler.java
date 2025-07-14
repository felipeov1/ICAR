package com.icar.platform.shared.exception;

import com.icar.platform.infrastructure.validation.exception.CustomValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldValidationError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new FieldValidationError(
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        "VALIDATION_ERROR"))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorResponse("Validation error", errors));
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handleCustomValidation(CustomValidationException ex) {
        List<FieldValidationError> errors = ex.getErrors().stream()
                .map(error -> new FieldValidationError(
                        error.getField(),
                        error.getMessage(),
                        error.getErrorCode()))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorResponse("Validation error", errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, List<String>> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.groupingBy(
                        violation -> violation.getPropertyPath().toString(),
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
                ));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Validation error", errors));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage(), ex.getErrors()));
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEntity(DuplicateEntityException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Validation error");
        body.put("timestamp", Instant.now());

        List<Object> errors = List.of(Map.of(
                "field", ex.getField(),
                "message", ex.getMessage(),
                "errorCode", ex.getErrorCode()
        ));

        body.put("errors", errors);
        return ResponseEntity.unprocessableEntity().body(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), null));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(ex.getMessage(), null));
    }

    @Getter
    @AllArgsConstructor
    public static class ValidationErrorResponse {
        private final String message;
        private final List<FieldValidationError> errors;
        private final Instant timestamp = Instant.now();
    }

    public record FieldValidationError(String field, String message, String errorCode) {}

    public record ErrorResponse(String message, Map<String, List<String>> errors) {}
}
