package com.aurindo.gym.api.v1.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class UserResponseModel extends RepresentationModel<UserResponse> {

    private String id;
    private String name;
    private String description;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime created;

    @Override
    public UserResponse add(Link link) {
        return super.add(link);
    }
}
