package com.icar.plataform.infrastructure.validation.validator.customer;

import com.icar.plataform.api.dto.request.CustomerCreateRequest;
import com.icar.plataform.domain.repository.CustomerRepository;
import com.icar.plataform.infrastructure.validation.exception.CustomValidationException;
import com.icar.plataform.infrastructure.validation.exception.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class CustomerValidator {

    private final CustomerRepository customerRepository;

    public void validateCreate(CustomerCreateRequest request) {
        List<ValidationError> errors = new ArrayList<>();

        // Full name validation
        validateField(errors, "fullName", request.getFullName(),
                value -> value == null || value.length() < 3 || value.length() > 255,
                "Full name must be between 3 and 255 characters",
                "INVALID_LENGTH"
        );

        // Password validations
        String password = request.getPassword();

        validateField(errors, "password", password,
                value -> value == null || value.length() < 8,
                "Password must be at least 8 characters long",
                "INVALID_PASSWORD_LENGTH"
        );

        validateField(errors, "password", password,
                value -> !value.matches(".*[A-Z].*"),
                "Password must contain at least one uppercase letter",
                "PASSWORD_MISSING_UPPERCASE"
        );

        validateField(errors, "password", password,
                value -> !value.matches(".*[0-9].*"),
                "Password must contain at least one number",
                "PASSWORD_MISSING_NUMBER"
        );

        validateField(errors, "password", password,
                value -> !value.matches(".*[!@#$%^&*].*"),
                "Password must contain at least one special character (!@#$%^&*)",
                "PASSWORD_MISSING_SPECIAL"
        );

        // Phone number validation
        validateField(errors, "phone", request.getPhone(),
                value -> value == null || !value.matches("^\\d{11}$"),
                "Phone number must contain exactly 11 digits",
                "INVALID_PHONE"
        );

        // Business rules (uniqueness checks)
        if (customerRepository.existsByEmail(request.getEmail())) {
            errors.add(ValidationError.builder()
                    .field("email")
                    .message("Email is already registered")
                    .errorCode("EMAIL_ALREADY_EXISTS")
                    .build());
        }

        if (customerRepository.existsByPhone(request.getPhone())) {
            errors.add(ValidationError.builder()
                    .field("phone")
                    .message("Phone number is already registered")
                    .errorCode("PHONE_ALREADY_EXISTS")
                    .build());
        }

        // Throw exception if any validation errors are found
        if (!errors.isEmpty()) {
            throw new CustomValidationException(errors);
        }
    }

    private void validateField(List<ValidationError> errors, String field, String value,
                               Predicate<String> condition, String message, String errorCode) {
        if (condition.test(value)) {
            errors.add(ValidationError.builder()
                    .field(field)
                    .message(message)
                    .errorCode(errorCode)
                    .build());
        }
    }
}
