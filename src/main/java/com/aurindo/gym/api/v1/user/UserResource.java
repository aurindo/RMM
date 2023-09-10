package com.aurindo.gym.api.v1.user;

import com.aurindo.gym.api.v1.user.model.UserResponse;
import com.aurindo.gym.api.v1.user.model.UserRequest;
import com.aurindo.gym.infrastructure.exception.BaseException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api/v1/users")
public interface UserResource {

    @GetMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<PagedModel<UserResponse>> fetchAll(
            final Pageable pageable
    );

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<UserResponse> create(
            @Valid @RequestBody UserRequest userRequest) throws BaseException;

    @GetMapping(
            value = "/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<UserResponse> getById(
            @PathVariable(value = "userId") String userId) throws BaseException;

    @DeleteMapping(
            value = "/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> delete(
            @PathVariable(value = "userId") String userId);

    @PutMapping(
            value = "/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<UserResponse> update(
            @Valid @RequestBody UserRequest updateRequest,
            @PathVariable(value = "userId", required = true) String id) throws BaseException;

}
