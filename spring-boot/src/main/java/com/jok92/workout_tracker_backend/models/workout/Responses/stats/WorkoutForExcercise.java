package com.jok92.workout_tracker_backend.models.workout.Responses.stats;

import com.jok92.workout_tracker_backend.models.workout.Responses.workouts.ScrubbedSetModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutForExcercise {
    LocalDate dateOfWorkout;
    List<ScrubbedSetModel> sets;
}
