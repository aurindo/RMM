package com.aurindo.gym.api.v1.user.model;

import com.aurindo.gym.domain.model.User;
import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserRequest {

    private String id;
    private String name;
    private String description;

    public User toUser() {
        return User.builder().
                id(this.id).
                description(this.description).
                name(this.name).
                build();
    }

}
