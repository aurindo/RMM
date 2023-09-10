package com.aurindo.gym.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.Date;

@Builder
@Getter
@Setter
@Entity(name = "userr")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    public User(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private String id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date created;

}
