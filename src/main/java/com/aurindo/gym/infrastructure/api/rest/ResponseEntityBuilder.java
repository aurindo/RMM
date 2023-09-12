package com.aurindo.gym.infrastructure.api.rest;

import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder {
    public static ResponseEntity<Object> build(RestAPIError restAPIError) {
        return new ResponseEntity<>(restAPIError, restAPIError.getStatus());
    }

}
