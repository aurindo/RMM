package com.aurindo.gym.api.v1.user.model;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class UserResponseModelAssembler extends RepresentationModelAssemblerSupport<UserResponse, UserResponseModel> {

    public UserResponseModelAssembler() {
        super(UserResponse.class, UserResponseModel.class);
    }

    @Override
    public UserResponseModel toModel(final UserResponse entity) {
        final UserResponseModel model = new UserResponseModel();
        BeanUtils.copyProperties(entity, model);
//        entity.getLinks().stream().forEach(link -> model.add(link));
        return model;
    }

}
