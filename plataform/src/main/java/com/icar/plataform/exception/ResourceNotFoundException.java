package com.icar.plataform.exception;
/**
    * ResourceNotFoundException
    * Ao buscar recursos
    * inexistente
    * ID inexistente
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}