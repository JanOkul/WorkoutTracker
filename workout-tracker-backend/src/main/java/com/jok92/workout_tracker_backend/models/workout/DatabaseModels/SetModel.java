package com.jok92.workout_tracker_backend.models.workout.DatabaseModels;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workout_id", nullable = false)
    private Long workoutId;

    @Column(name = "exercise_id")
    private Long exerciseId;

    @Column(name = "set_number", nullable = false)
    private Short setNumber;

    @Column(nullable = false)
    private Integer weight;

    @Column(nullable = false)
    private Short reps;

  /*  public SetModel createSetModel(Long id, Long workoutId, Long workoutId, Integer setNumber, Integer weight, Integer reps) {
        return new SetModel(

        )
    }*/
}