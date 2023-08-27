package com.aurindo.gym.domain.model;

import jakarta.persistence.*;
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

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private String id;
    private String name;
    private String description;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date created;

}
