package com.jok92.workout_tracker_backend.repositories;

import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.WorkoutsModel;
import com.jok92.workout_tracker_backend.models.workout.Responses.stats.WorkoutForExcercise;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkoutsRepo extends JpaRepository<WorkoutsModel, Long> {
    Optional<WorkoutsModel> findByUserIdAndDateOfWorkout(UUID uuid, LocalDate date);


}
