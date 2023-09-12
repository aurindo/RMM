package com.aurindo.gym.infrastructure.api.rest;

import com.aurindo.gym.infrastructure.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice(basePackages = "com.aurindo.gym.api")
public class RestAPIExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleMyException(EntityNotFoundException ex) {

        RestAPIError err = new RestAPIError(
                ZonedDateTime.now(),
                HttpStatus.NOT_FOUND,
                ex.getMessage() ,
                null);

        return ResponseEntityBuilder.build(err);

    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleUnexpectedException(Throwable ex) {

        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        RestAPIError err = new RestAPIError(
                ZonedDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() ,
                details);

        return ResponseEntityBuilder.build(err);
    }

}
