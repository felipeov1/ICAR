package com.icar.platform.infrastructure.validation.validator;

public interface ValidatorInterface<T> {
    void validate(T request);
}