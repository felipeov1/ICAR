package com.icar.plataform.infrastructure.validation.validator;

public interface ValidatorInterface<T> {
    void validate(T request);
}