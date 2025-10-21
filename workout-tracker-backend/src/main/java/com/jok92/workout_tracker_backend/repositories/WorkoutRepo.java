package com.jok92.workout_tracker_backend.repositories;

import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.WorkoutModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkoutRepo extends JpaRepository<WorkoutModel, Long> {
    Optional<WorkoutModel> findByUserIdAndDateOfWorkout(UUID uuid, LocalDate date);
}
