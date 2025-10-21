package com.jok92.workout_tracker_backend.models.workout.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteExercise {
    Integer exerciseId;
    Integer setNumber;
}
