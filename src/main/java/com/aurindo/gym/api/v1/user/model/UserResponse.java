package com.aurindo.gym.api.v1.user.model;

import com.aurindo.gym.api.v1.user.UserController;
import com.aurindo.gym.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@EqualsAndHashCode(callSuper=false)
@Getter
@Builder
@AllArgsConstructor
public class UserResponse extends RepresentationModel<UserResponse> {

    private String id;
    private String name;
    private String description;
    private ZonedDateTime created;

    public static UserResponse fromUser(User user) {
        UserResponse userResponse = UserResponse.builder().
                name(user.getName()).
                description(user.getDescription()).
                created(user.getCreated()).
                id(user.getId()).build();

        userResponse.add(linkTo(methodOn(UserController.class).getById(user.getId())).withSelfRel());
        userResponse.add(linkTo(methodOn(UserController.class).delete(user.getId())).withRel("delete"));

        return userResponse;
    }

}
