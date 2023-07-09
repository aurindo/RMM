package com.aurindo.gym.api.v1.user.model;

import com.aurindo.gym.api.v1.user.UserController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserResponseModelAssembler implements RepresentationModelAssembler<UserResponse, EntityModel<UserResponse>> {

    @Override
    public EntityModel<UserResponse> toModel(final UserResponse userResponse) {
        return EntityModel.of(userResponse,
                linkTo(methodOn(UserController.class).getById(userResponse.getId())).withSelfRel());
    }

    @Override
    public CollectionModel toCollectionModel(Iterable entities) {
        return null;
    }
}
