package com.jok92.workout_tracker_backend.repositories;

import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.SetModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SetRepo extends JpaRepository<SetModel, Long> {

    List<SetModel> findAllByWorkoutId(Long workoutId);
    Optional<SetModel> findByWorkoutIdAndExerciseIdAndSetNumber(Long workoutId, String exerciseId, Short setNumber);
    void deleteByWorkoutIdAndSetNumber(Long workoutId, Short setNumber);
    Long countByWorkoutId(Long workoutId);
}
