package com.jok92.workout_tracker_backend.models.workout.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateExercise {
    private Integer oldExerciseId;
    private Integer oldSetNumber;
    private Integer exerciseId;
    private Integer setNumber;
    private Integer weight;
    private Integer reps;
}
