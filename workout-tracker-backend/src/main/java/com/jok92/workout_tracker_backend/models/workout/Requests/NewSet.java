package com.jok92.workout_tracker_backend.models.workout.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewSet {
    private String exerciseId;
    private Short setNumber;
    private Integer weight;
    private Short reps;
}
