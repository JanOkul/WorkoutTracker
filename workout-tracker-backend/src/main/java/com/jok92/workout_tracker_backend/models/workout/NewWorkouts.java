package com.jok92.workout_tracker_backend.models.workout;

import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.ExerciseModel;
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
    LocalDate dateOfWorkout;
    Integer exerciseId;
    List<NewExercise> newExercises;
}
