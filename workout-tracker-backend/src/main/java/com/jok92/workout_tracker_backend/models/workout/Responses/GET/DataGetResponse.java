package com.jok92.workout_tracker_backend.models.workout.Responses.GET;

import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.ExerciseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Getter
@AllArgsConstructor
public class DataGetResponse {
    public LocalDate dateOfWorkout;
    public List<ScrubbedExerciseModel> exercises;
}
