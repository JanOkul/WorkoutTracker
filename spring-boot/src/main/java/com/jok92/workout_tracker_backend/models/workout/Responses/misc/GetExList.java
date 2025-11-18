package com.jok92.workout_tracker_backend.models.workout.Responses.misc;

import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.ExerciseModel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetExList {
    List<ExerciseModel> exercises;
}
