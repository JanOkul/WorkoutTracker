package com.jok92.workout_tracker_backend.models.workout.Responses.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkoutEntry {
    LocalDate dateOfWorkout;
    short setNumber;
    int weight;
    short reps;
}
