package com.icar.platform.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.Map;
import java.util.List;

/**
 * Para erros de validação de dados de entrada
 * HTTP Status: 400 Bad Request
 */
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends BusinessException {
    private final Map<String, List<String>> errors;

    public ValidationException(Map<String, List<String>> errors) {
        super("Validation failed");
        this.errors = errors;
    }

}