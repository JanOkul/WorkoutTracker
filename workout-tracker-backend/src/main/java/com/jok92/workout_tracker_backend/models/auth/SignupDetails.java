package com.jok92.workout_tracker_backend.models.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupDetails implements AuthDetails{
    private String displayName;
    private String email;
    private String password;
    private Integer age;
    private Integer weight;
}
