package com.aurindo.gym.api.v1.user;

import com.aurindo.gym.api.v1.user.model.UserRequest;
import com.aurindo.gym.api.v1.user.model.UserResponse;
import com.aurindo.gym.api.v1.user.model.UserResponseModelAssembler;
import com.aurindo.gym.domain.model.User;
import com.aurindo.gym.domain.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@AllArgsConstructor
@RestController
public class UserController implements UserResource {

    private final int PAGE_LIMIT_HOME = 10;

    private final PagedResourcesAssembler pagedResourcesAssembler;

    private final UserResponseModelAssembler userResponseModelAssembler;

    private final UserService userService;

    @Override
    public ResponseEntity<UserResponse> create(final UserRequest createRequest) {
        final User user = createRequest.toUser();
        final User userCreated = userService.save(user);
        final UserResponse userResponse = UserResponse.fromUser(userCreated);

        userResponse.add(linkTo(methodOn(UserController.class).getById(userCreated.getId())).withSelfRel());
        userResponse.add(linkTo(methodOn(UserController.class).delete(userCreated.getId())).withRel("delete"));
        final URI uri = userResponse.getLink("self").get().toUri();

        return ResponseEntity.created(uri).body(userResponse);
    }

    @Override
    public ResponseEntity<UserResponse> getById(final String id) {

        final User userReturned = userService.findById(id);

        final UserResponse userResponse = UserResponse.fromUser(userReturned);

        userResponse.add(linkTo(methodOn(UserController.class).getById(userReturned.getId())).withSelfRel());
        userResponse.add(linkTo(methodOn(UserController.class).delete(userReturned.getId())).withRel("delete"));

        return ResponseEntity.ok(userResponse);
    }

    @Override
    public ResponseEntity fetchAll(
            final int page,
            final int pageSize
    ) {
        final Pageable pageable = PageRequest.of(page - 1, pageSize);
        final Page<User> pageUser = userService.fetchAll(pageable, PAGE_LIMIT_HOME);

        final Page<UserResponse> userResponsePage = mountPage(pageable, pageUser);

        return ResponseEntity.
                ok().
                contentType(MediaTypes.HAL_JSON).
                body(pagedResourcesAssembler.toModel(userResponsePage, userResponseModelAssembler));
    }

    private Page<UserResponse> mountPage(final Pageable pageable, final Page<User> page) {
        final List<UserResponse> responseList = page.get().map(
                user -> UserResponse.fromUser(user)).collect(Collectors.toList());

        final Page<UserResponse> responsePage =
                new PageImpl<>(responseList, pageable, page.getTotalElements());
        return responsePage;
    }

    @Override
    public ResponseEntity<?> delete(final String id) {

        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserResponse> update(
            UserRequest updateRequest,
            String id) {

        final User user = updateRequest.toUser();
        user.setId(id);

        final User userUpdated = userService.update(user);

        final UserResponse userResponse = UserResponse.fromUser(userUpdated);

        userResponse.add(linkTo(methodOn(UserController.class).getById(userUpdated.getId())).withSelfRel());
        userResponse.add(linkTo(methodOn(UserController.class).delete(userUpdated.getId())).withRel("delete"));

        return ResponseEntity.ok().contentType(MediaTypes.HAL_JSON).body(userResponse);
    }

}
