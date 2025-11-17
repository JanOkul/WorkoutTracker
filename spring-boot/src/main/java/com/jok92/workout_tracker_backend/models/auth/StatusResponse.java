package com.jok92.workout_tracker_backend.models.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusResponse {
    Boolean isAuthenticated;
}
