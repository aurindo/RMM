package com.aurindo.gym.api.v1.user;

import com.aurindo.gym.api.v1.user.model.UserRequest;
import com.aurindo.gym.api.v1.user.model.UserResponse;
import com.aurindo.gym.api.v1.user.model.UserResponseModelAssembler;
import com.aurindo.gym.domain.model.User;
import com.aurindo.gym.domain.user.UserService;
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
    ) {
        final User user = new User(createRequest.name(), createRequest.description());
        final User userCreated = userService.save(user);
        final UserResponse userResponse = UserResponse.fromUser(userCreated);

        final URI uri = userResponse.getLink("self").get().toUri();

        return ResponseEntity.created(uri).body(userResponse);
    }

    @Override
    public ResponseEntity<UserResponse> getById(
            final String id
    ) {

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
    ) {
        final User user = new User(updateRequest.name(), updateRequest.description());
        user.setId(id);

        final User userUpdated = userService.update(user);

        final UserResponse userResponse = UserResponse.fromUser(userUpdated);

        return ResponseEntity.ok().contentType(MediaTypes.HAL_JSON).body(userResponse);
    }
}
