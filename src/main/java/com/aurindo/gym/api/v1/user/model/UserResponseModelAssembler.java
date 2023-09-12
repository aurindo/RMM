package com.aurindo.gym.api.v1.user.model;

import com.aurindo.gym.api.v1.user.UserController;
import com.aurindo.gym.domain.model.User;
import com.aurindo.gym.domain.util.DateUtil;
import com.aurindo.gym.infrastructure.exception.BaseException;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserResponseModelAssembler extends RepresentationModelAssemblerSupport<User, UserResponse> {

    public UserResponseModelAssembler() {
        super(UserController.class, UserResponse.class);
    }

    @Override
    public UserResponse toModel(final User entity)
    {
        final UserResponse model = new UserResponse();
        BeanUtils.copyProperties(entity, model);
        try {
            model.setCreated(DateUtil.dateToZonedDateTime(entity.getCreated()));
        } catch (BaseException e) {
            //Supress exception
        }
        try {
            model.add(linkTo(methodOn(UserController.class).getById(entity.getId())).withSelfRel());
        } catch (BaseException e) {
            throw new RuntimeException(e);
        }
        try {
            model.add(linkTo(methodOn(UserController.class).delete(entity.getId())).withRel("delete"));
        } catch (BaseException e) {
            throw new RuntimeException(e);
        }
        return model;
    }

}
