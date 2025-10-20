package com.jok92.workout_tracker_backend.models.workout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewExercise {
    private Integer setNumber;
    private Integer weight;
    private Integer reps;
}
