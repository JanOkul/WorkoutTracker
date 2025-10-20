package com.jok92.workout_tracker_backend.models.workout.DatabaseModels;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="display_name", nullable = false)
    private String displayName;

    @Column(nullable = false)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Integer weight;

    // Constructor for all non DB auto-generated values.
    public UserModel(String displayName, String email, String password, Integer age, Integer weight) {
        this.displayName = displayName;
        this.email = email;
        this.password = password;
        this.age = age;
        this.weight = weight;
    }
}
