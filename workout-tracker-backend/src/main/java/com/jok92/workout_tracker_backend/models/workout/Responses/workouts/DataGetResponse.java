package com.jok92.workout_tracker_backend.models.workout.Responses.workouts;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class DataGetResponse {
    public LocalDate dateOfWorkout;
    public List<GroupedExerciseSets> exercises;
}
