package com.jok92.workout_tracker_backend.repositories;

import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.SetModel;
import com.jok92.workout_tracker_backend.models.workout.Responses.stats.WorkoutEntry;
import org.springframework.data.repository.query.Param;  // ✅ CORRECT

import org.hibernate.jdbc.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SetRepo extends JpaRepository<SetModel, Long> {

    List<SetModel> findAllByWorkoutId(Long workoutId);
    Optional<SetModel> findByWorkoutIdAndExerciseIdAndSetNumber(Long workoutId, String exerciseId, Short setNumber);
    void deleteByWorkoutIdAndSetNumber(Long workoutId, Short setNumber);
    Long countByWorkoutId(Long workoutId);

    @Query("""
            SELECT new com.jok92.workout_tracker_backend.models.workout.Responses.stats.WorkoutEntry( s.workout.dateOfWorkout, s.setNumber, s.weight, s.reps)
            FROM SetModel s
            WHERE s.workout.user.id = :userId
            AND s.exercise.id = :exerciseName
            AND s.workout.dateOfWorkout >= :startDate
            """)
    List<WorkoutEntry>  findWorkoutsByUUIDandExerciseNameSinceStartDate(
            @Param("userId") UUID uuid,
            @Param("exerciseName") String exerciseName,
            @Param("startDate") LocalDate startDate
            );

}



