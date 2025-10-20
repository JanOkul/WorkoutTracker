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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workout_id", nullable = false)
    private Long workoutId;

    @Column(name = "exercise_id", nullable = false)
    private Integer exerciseId;

    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    @Column(nullable = false)
    private Integer weight;

    @Column(nullable = false)
    private Integer reps;
}