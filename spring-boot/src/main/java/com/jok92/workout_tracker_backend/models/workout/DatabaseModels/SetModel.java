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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    private WorkoutsModel workout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private ExerciseModel exercise;

    @Column(name = "set_number", nullable = false)
    private Short setNumber;

    @Column(nullable = false)
    private Integer weight;

    @Column(nullable = false)
    private Short reps;
}