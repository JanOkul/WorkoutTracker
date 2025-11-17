package com.jok92.workout_tracker_backend.models.workout.Responses.misc;


import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Value
@Builder
public class ExceptionResponse {
    LocalDateTime timestamp = LocalDateTime.now();
    HttpStatus status;
    String error;
}

