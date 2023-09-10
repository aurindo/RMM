package com.aurindo.gym.api.v1.user;

import com.aurindo.gym.api.v1.user.model.UserResponse;
import com.aurindo.gym.api.v1.user.model.UserRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
public interface UserResource {

    String ROUTE = "/api/v1/users";

    @GetMapping(
            value = ROUTE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<PagedModel<UserResponse>> fetchAll(
            Pageable pageable
    );

    @PostMapping(
            value = ROUTE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<UserResponse> create(
            @Valid @RequestBody UserRequest userRequest);

    @GetMapping(
            value = ROUTE + "/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<UserResponse> getById(
            @PathVariable(value = "userId") String userId);

    @DeleteMapping(
            value = ROUTE + "/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> delete(
            @PathVariable(value = "userId") String userId);

    @PutMapping(
            value = ROUTE + "/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<UserResponse> update(
            @Valid @RequestBody UserRequest updateRequest,
            @PathVariable(value = "userId", required = true) String id);
}
