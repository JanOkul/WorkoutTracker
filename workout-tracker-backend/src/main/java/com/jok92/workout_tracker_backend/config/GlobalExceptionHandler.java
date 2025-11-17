package com.jok92.workout_tracker_backend.config;

import com.jok92.workout_tracker_backend.models.workout.Responses.misc.ExceptionResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionResponse> handleExpiredJwt(ExpiredJwtException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(
                ExceptionResponse.builder()
                        .status(status)
                        .error(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<ExceptionResponse> handlePropertyValue(PropertyValueException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                ExceptionResponse.builder()
                        .status(status)
                        .error(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFound(EntityNotFoundException e) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
