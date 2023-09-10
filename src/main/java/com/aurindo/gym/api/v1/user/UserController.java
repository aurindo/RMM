package com.aurindo.gym.api.v1.user;

import com.aurindo.gym.api.v1.user.model.UserRequest;
import com.aurindo.gym.api.v1.user.model.UserResponse;
import com.aurindo.gym.api.v1.user.model.UserResponseModelAssembler;
import com.aurindo.gym.domain.model.User;
import com.aurindo.gym.domain.user.UserService;
import com.aurindo.gym.infrastructure.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController implements UserResource {

    @Autowired
    private UserResponseModelAssembler userResponseModelAssembler;

    @Autowired
    private PagedResourcesAssembler<User> pagedResourcesAssembler;

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<UserResponse> create(
            final UserRequest createRequest
    ) throws BaseException {
        final User user = createRequest.toUser();
        final User userCreated = userService.save(user);
        final UserResponse userResponse = UserResponse.fromUser(userCreated);

        final URI uri = userResponse.getLink("self").get().toUri();

        return ResponseEntity.created(uri).body(userResponse);
    }

    @Override
    public ResponseEntity<UserResponse> getById(
            final String id
    ) throws BaseException {

        final User userReturned = userService.findById(id);

        final UserResponse userResponse = UserResponse.fromUser(userReturned);

        return ResponseEntity.ok(userResponse);
    }

    @Override
    public ResponseEntity<PagedModel<UserResponse>> fetchAll(
            final Pageable pageable
    ) {
        final Page<User> userEntities = userService.fetchAll(pageable);

        final PagedModel<UserResponse> pagedModel = pagedResourcesAssembler
                .toModel(userEntities, userResponseModelAssembler);

        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> delete(
            final String id
    ) {

        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserResponse> update(
            final UserRequest updateRequest,
            final String id
    ) throws BaseException {

        final User user = updateRequest.toUser();
        user.setId(id);

        final User userUpdated = userService.update(user);

        final UserResponse userResponse = UserResponse.fromUser(userUpdated);

        userResponse.add(linkTo(methodOn(UserController.class).getById(userUpdated.getId())).withSelfRel());
        userResponse.add(linkTo(methodOn(UserController.class).delete(userUpdated.getId())).withRel("delete"));

        return ResponseEntity.ok().contentType(MediaTypes.HAL_JSON).body(userResponse);
    }

}
