package com.jok92.workout_tracker_backend.services;

import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.ExerciseModel;
import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.WorkoutModel;
import com.jok92.workout_tracker_backend.models.workout.Requests.DeleteExercise;
import com.jok92.workout_tracker_backend.models.workout.Requests.NewExercise;
import com.jok92.workout_tracker_backend.models.workout.Requests.NewWorkouts;
import com.jok92.workout_tracker_backend.models.workout.Requests.UpdateExercise;
import com.jok92.workout_tracker_backend.models.workout.Responses.GET.ScrubbedExerciseModel;
import com.jok92.workout_tracker_backend.repositories.ExerciseRepo;
import com.jok92.workout_tracker_backend.repositories.UserRepo;
import com.jok92.workout_tracker_backend.repositories.WorkoutRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DataService {
    UserRepo userRepo;
    ExerciseRepo exerciseRepo;
    WorkoutRepo workoutRepo;

    @Autowired
    public DataService(UserRepo userRepo, WorkoutRepo workoutRepo, ExerciseRepo exerciseRepo) {
        this.userRepo = userRepo;
        this.exerciseRepo = exerciseRepo;
        this.workoutRepo = workoutRepo;
    }


    /**
     * Retrieves all exercises for a given day.
     * @param userId The target user's UUID.
     * @param date The date the exercises should be retrieved from.
     * @return A list of exercises. If no exercises were found, it returns an empty list.
     */
    public List<ScrubbedExerciseModel> getWorkoutsForDate(UUID userId, LocalDate date) {
        verifyDate(date);

        Optional<WorkoutModel> targetWorkoutModel = workoutRepo.findByUserIdAndDateOfWorkout(userId, date);

        // If a workout isn't recorded, then there cannot be any exercises;
        if (targetWorkoutModel.isEmpty()) {
            return new ArrayList<>();
        }

        List<ExerciseModel> exerciseModels = exerciseRepo.findAllByWorkoutId(targetWorkoutModel.get().getId());
        // Remove all the database identifiers from the response.
        return exerciseModels.stream()
                .map(ScrubbedExerciseModel::fromExerciseModel)
                .toList();
    }

    /**
     * Adds single or multiple exercises for a single exercise type.
     * @param userId The target user's UUID
     * @param date The workout that the exercises should be added to.
     * @param newWorkout Data from the client on when and what exercises to add.
     * @return The exercise added to the database.
     */
    @Transactional
    public List<ScrubbedExerciseModel> addExercisesForDate(UUID userId, LocalDate date, NewWorkouts newWorkout) {
        verifyDate(date);

        // Check if workout exists. If empty create new workout.
        WorkoutModel workout = workoutRepo.findByUserIdAndDateOfWorkout(userId, date)
                .orElseGet(() -> workoutRepo.save(new WorkoutModel(null, userId, date)));

        // Add database information to request data.
        List<NewExercise> newExercises = newWorkout.getNewExercises();
        List<ExerciseModel> exerciseModels = newExercises.stream()
                .map(newExercise ->
                        new ExerciseModel(
                                null,
                                workout.getId(),
                                newWorkout.getExerciseId(),
                                newExercise.getSetNumber(),
                                newExercise.getWeight(),
                                newExercise.getReps()
                                )
                ).toList();

        List<ExerciseModel> dbResponse = exerciseRepo.saveAll(exerciseModels);

        // Remove database information from database response
        return dbResponse.stream()
                .map(ScrubbedExerciseModel::fromExerciseModel)
                .toList();

    }


    /**
     * Changes a single exercise entry.
     * @param userId The target user's UUID
     * @param date The date of the workout the target exercise is in
     * @param updateExercise What to replace the existing exercise with.
     * @return The new exercise entry.
     */
    @Transactional
    public ScrubbedExerciseModel changeExerciseForDate(UUID userId, LocalDate date, UpdateExercise updateExercise) {
        verifyDate(date);

        // Check that the workout exists
        WorkoutModel workout = workoutRepo.findByUserIdAndDateOfWorkout(userId, date)
                .orElseThrow(() -> new EntityNotFoundException("No workouts registered for: " + date.toString()));

        // Check that the exercise exists
        ExerciseModel exercise = exerciseRepo.findByWorkoutIdAndExerciseIdAndSetNumber(
                workout.getId(), updateExercise.getOldExerciseId(), updateExercise.getOldSetNumber())
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));

        exercise.setExerciseId(updateExercise.getExerciseId());
        exercise.setSetNumber(updateExercise.getSetNumber());
        exercise.setWeight(updateExercise.getWeight());
        exercise.setReps(updateExercise.getReps());

        return ScrubbedExerciseModel.fromExerciseModel(exerciseRepo.save(exercise));
    }


    /**
     * Deletes an exercise from a workout, if the workout has no more exercises, it is also deleted.
     * @param userId The target user's UUID
     * @param date The date the exercise to be removed is on
     * @param deleteExercise Target exercise details
     */
    @Transactional
    public void deleteExerciseForDate(UUID userId, LocalDate date, DeleteExercise deleteExercise) {
        verifyDate(date);

        WorkoutModel workout = workoutRepo.findByUserIdAndDateOfWorkout(userId, date)
                .orElseThrow(() -> new EntityNotFoundException("No workout corresponding to id and date"));

        exerciseRepo.deleteByWorkoutIdAndSetNumber(workout.getId(), deleteExercise.getSetNumber());

        // If there are no more exercises corresponding to a workout, remove the workout
        long numExercises = exerciseRepo.countByWorkoutId(workout.getId());

        if (numExercises == 0) {
            workoutRepo.deleteById(workout.getId());
        }
    }

    /**
     * Checks that requests aren't in the future.
     * @param date The date the request is for.
     */
    static void verifyDate(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Time travellers must be patient"
            );
        }
    }
}
