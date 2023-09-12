package com.aurindo.gym.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends BaseException {

    private static final String DEFAULT_MESSAGE = "%s not found. Parameters: %s";

    public EntityNotFoundException(Class clazz, String parameters) {
        super(String.format(DEFAULT_MESSAGE, clazz.getSimpleName(), parameters));
    }

    public EntityNotFoundException(Class clazz, Throwable throwable, String parameters) {
        super(String.format(DEFAULT_MESSAGE, clazz.getSimpleName(), parameters), throwable);
    }

}
