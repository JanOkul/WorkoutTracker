package com.jok92.workout_tracker_backend.models.workout.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewWorkouts {
    Integer exerciseId;
    List<NewExercise> newExercises;
}
