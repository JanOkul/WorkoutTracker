package com.jok92.workout_tracker_backend.models.workout.DatabaseModels;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workouts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "date_of_workout", nullable = false)
    private LocalDate dateOfWorkout;
}