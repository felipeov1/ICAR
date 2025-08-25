package com.icar.platform.infrastructure.validation.validator.customer;

import com.icar.platform.api.dto.request.auth.RegisterCustomerRequest;
import com.icar.platform.domain.repository.customer.CustomerRepository;
import com.icar.platform.infrastructure.validation.exception.CustomValidationException;
import com.icar.platform.infrastructure.validation.exception.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class CustomerValidator {

    private final CustomerRepository customerRepository;

    public void validateCreate(RegisterCustomerRequest request) {
        List<ValidationError> errors = new ArrayList<>();

        validateField(errors, "fullName", request.fullName(),
                value -> value == null || value.length() < 3 || value.length() > 255,
                "Full name must be between 3 and 255 characters",
                "INVALID_LENGTH"
        );

        String password = request.password();

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
                "Password must contain at least one getStreetNumber",
                "PASSWORD_MISSING_NUMBER"
        );

        validateField(errors, "phone", request.phone(),
                value -> value == null || !value.matches("^\\d{11}$"),
                "Phone getStreetNumber must contain exactly 11 digits",
                "INVALID_PHONE"
        );

        if (customerRepository.existsByEmail(request.email())) {
            errors.add(ValidationError.builder()
                    .field("email")
                    .message("Email is already registered")
                    .errorCode("EMAIL_ALREADY_EXISTS")
                    .build());
        }

        if (customerRepository.existsByPhone(request.phone())) {
            errors.add(ValidationError.builder()
                    .field("phone")
                    .message("Phone getStreetNumber is already registered")
                    .errorCode("PHONE_ALREADY_EXISTS")
                    .build());
        }

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
