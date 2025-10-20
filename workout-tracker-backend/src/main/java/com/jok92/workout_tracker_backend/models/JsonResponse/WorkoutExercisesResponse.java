package com.jok92.workout_tracker_backend.models.JsonResponse;

import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.ExerciseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class WorkoutExercisesResponse {
    public List<ExerciseModel> exercises;
}
