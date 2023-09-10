package com.aurindo.gym.api.v1.user.model;

import com.aurindo.gym.api.v1.user.UserController;
import com.aurindo.gym.domain.model.User;
import com.aurindo.gym.domain.util.DateUtil;
import com.aurindo.gym.infrastructure.exception.BaseException;
import com.aurindo.gym.infrastructure.exception.EntityNotFoundException;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@EqualsAndHashCode(callSuper=false)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse extends RepresentationModel<UserResponse> {

    private String id;
    private String name;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_PATTERN)
    private ZonedDateTime created;

    public static UserResponse fromUser(final User user) throws BaseException {
        final UserResponse userResponse = UserResponse.builder().
                name(user.getName()).
                description(user.getDescription()).
                created(DateUtil.dateToZonedDateTime(user.getCreated())).
                id(user.getId()).build();

        userResponse.add(linkTo(methodOn(UserController.class).getById(user.getId())).withSelfRel());
        userResponse.add(linkTo(methodOn(UserController.class).delete(user.getId())).withRel("delete"));

        return userResponse;
    }

}
