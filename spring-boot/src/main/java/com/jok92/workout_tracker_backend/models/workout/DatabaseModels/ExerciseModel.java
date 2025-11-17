package com.jok92.workout_tracker_backend.models.workout.DatabaseModels;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseModel {

    @Id
    private String id;

    private String description;
}
