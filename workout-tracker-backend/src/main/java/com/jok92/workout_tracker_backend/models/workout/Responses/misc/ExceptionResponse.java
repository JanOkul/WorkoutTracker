package com.jok92.workout_tracker_backend.models.workout.Responses.misc;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Value
@Builder
public class ExceptionResponse {
    LocalDateTime timestamp = LocalDateTime.now();
    HttpStatus status;
    String error;
}

