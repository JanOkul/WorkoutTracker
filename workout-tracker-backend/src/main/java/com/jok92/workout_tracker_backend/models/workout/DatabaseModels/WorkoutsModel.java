package com.jok92.workout_tracker_backend.models.workout.DatabaseModels;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workouts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @Column(name = "date_of_workout", nullable = false)
    private LocalDate dateOfWorkout;
}