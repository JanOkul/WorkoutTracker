package com.jok92.workout_tracker_backend.models.workout.Responses.workouts;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DataGetResponse {
    public LocalDate dateOfWorkout;
    public List<GroupedSets> exercises;
}
