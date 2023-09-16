package com.aurindo.gym.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WrongParameterException extends BaseException {

    public WrongParameterException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
