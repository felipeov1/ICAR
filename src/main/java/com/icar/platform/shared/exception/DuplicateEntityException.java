package com.icar.platform.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Quando uma entidade duplicada é detectada (email, CPF, etc.)
 * HTTP Status: 409 Conflict
 */
@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEntityException extends BusinessException {
    private final String field;
    private final String errorCode;

    public DuplicateEntityException(String field, String message, String errorCode) {
        super(message);
        this.field = field;
        this.errorCode = errorCode;
    }

}
