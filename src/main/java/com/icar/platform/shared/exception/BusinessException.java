package com.icar.platform.shared.exception;

/**
 * Exceção base para erros de regras de negócio
 * HTTP Status: Geralmente 400-499 (dependendo da subclasse)
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}