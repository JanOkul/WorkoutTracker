package com.jok92.workout_tracker_backend.repositories;

import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.ExerciseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepo extends JpaRepository<ExerciseModel, String> {
}

