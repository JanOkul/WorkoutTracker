package com.jok92.workout_tracker_backend.models.workout.Responses.workouts;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class DataGetResponse {
    public LocalDate dateOfWorkout;
    public List<GroupedSets> exercises;
}
