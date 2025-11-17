package com.jok92.workout_tracker_backend.models.workout.Responses.workouts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupedSets {
    String exerciseId;

    List<ScrubbedSetModel> sets;
}
