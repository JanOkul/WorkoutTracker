package com.jok92.workout_tracker_backend.repositories;

import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.ExerciseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepo extends JpaRepository<ExerciseModel, Long> {

    List<ExerciseModel> findAllByWorkoutId(Long workoutId);
    List<ExerciseModel> findAllByWorkoutIdAndExerciseId(Long workoutId, Integer exerciseId);
}
