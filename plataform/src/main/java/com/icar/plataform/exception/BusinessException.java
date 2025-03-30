package com.icar.plataform.exception;
/**
 * BusinessException
 * Regras de negócio
 * Durante a operação
 * Cancelamento tardio
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}