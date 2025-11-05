package com.jok92.workout_tracker_backend.services;

import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.ExerciseModel;
import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.WorkoutsModel;
import com.jok92.workout_tracker_backend.models.workout.Requests.DeleteExercise;
import com.jok92.workout_tracker_backend.models.workout.Requests.NewExercise;
import com.jok92.workout_tracker_backend.models.workout.Requests.NewWorkouts;
import com.jok92.workout_tracker_backend.models.workout.Requests.UpdateExercise;
import com.jok92.workout_tracker_backend.models.workout.Responses.workouts.DataGetResponse;
import com.jok92.workout_tracker_backend.models.workout.Responses.workouts.GroupedExerciseSets;
import com.jok92.workout_tracker_backend.models.workout.Responses.workouts.ScrubbedExerciseModel;
import com.jok92.workout_tracker_backend.repositories.ExerciseRepo;
import com.jok92.workout_tracker_backend.repositories.UserRepo;
import com.jok92.workout_tracker_backend.repositories.WorkoutsRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkoutService {
    UserRepo userRepo;
    ExerciseRepo exerciseRepo;
    WorkoutsRepo workoutsRepo;

    @Autowired
    public WorkoutService(UserRepo userRepo, WorkoutsRepo workoutsRepo, ExerciseRepo exerciseRepo) {
        this.userRepo = userRepo;
        this.exerciseRepo = exerciseRepo;
        this.workoutsRepo = workoutsRepo;
    }


    /**
     * Retrieves all exercises for a given day.
     * @param userId The target user's UUID.
     * @param date The date the exercises should be retrieved from.
     * @return A list of exercises. If no exercises were found, it returns an empty list.
     */
    public DataGetResponse getWorkoutsForDate(UUID userId, LocalDate date) {
        verifyDate(date);

        Optional<WorkoutsModel> targetWorkoutModel = workoutsRepo.findByUserIdAndDateOfWorkout(userId, date);

        // If a workout isn't recorded, then there cannot be any exercises;
        if (targetWorkoutModel.isEmpty()) {
            return new DataGetResponse(date, new ArrayList<>());
        }

        List<ExerciseModel> exerciseModels = exerciseRepo.findAllByWorkoutId(targetWorkoutModel.get().getId());
        // Remove all the database identifiers from the response.
        return new DataGetResponse(
                date,
                groupExerciseSets(exerciseModels)
        );
    }

    /**
     * Adds single or multiple exercises for a single exercise type.
     * @param userId The target user's UUID
     * @param date The workout that the exercises should be added to.
     * @param newExercise Data from the client on when and what exercises to add.
     * @return The exercise added to the database.
     */
    @Transactional
    public ScrubbedExerciseModel addExercisesForDate(UUID userId, LocalDate date, NewExercise newExercise) {
        verifyDate(date);

        // Check if workout exists. If empty create new workout.
        WorkoutsModel workout = workoutsRepo.findByUserIdAndDateOfWorkout(userId, date)
                .orElseGet(() -> workoutsRepo.save(new WorkoutsModel(null, userId, date)));

        ExerciseModel exerciseModel = new ExerciseModel(
                                null,
                                workout.getId(),
                                newExercise.getExerciseId(),
                                newExercise.getSetNumber(),
                                newExercise.getWeight(),
                                newExercise.getReps()
        );

        ExerciseModel dbResponse = exerciseRepo.save(exerciseModel);

        // Remove database information from database response
        return ScrubbedExerciseModel.fromExerciseModel(dbResponse);

    }


    /**
     * Changes a single exercise entry.
     *
     * @param userId         The target user's UUID
     * @param date           The date of the workout the target exercise is in
     * @param updateExercise What to replace the existing exercise with.
     * @return The new exercise entry.
     */
    @Transactional
    public UpdateExercise changeExerciseForDate(UUID userId, LocalDate date, UpdateExercise updateExercise) {
        verifyDate(date);

        // Check that the workout exists
        WorkoutsModel workout = workoutsRepo.findByUserIdAndDateOfWorkout(userId, date)
                .orElseThrow(() -> new EntityNotFoundException("No workouts registered for: " + date.toString()));

        // Check that the exercise exists
        ExerciseModel exercise = exerciseRepo.findByWorkoutIdAndExerciseIdAndSetNumber(
                workout.getId(), updateExercise.getExerciseId(), updateExercise.getSetNumber())
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));

        exercise.setExerciseId(updateExercise.getExerciseId());
        exercise.setSetNumber(updateExercise.getSetNumber());
        exercise.setWeight(updateExercise.getWeight());
        exercise.setReps(updateExercise.getReps());

        return new UpdateExercise(
                exercise.getExerciseId(),
                exercise.getSetNumber(),
                exercise.getWeight(),
                exercise.getReps()
        );
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

        WorkoutsModel workout = workoutsRepo.findByUserIdAndDateOfWorkout(userId, date)
                .orElseThrow(() -> new EntityNotFoundException("No workout corresponding to id and date"));

        exerciseRepo.deleteByWorkoutIdAndSetNumber(workout.getId(), deleteExercise.getSetNumber());

        // If there are no more exercises corresponding to a workout, remove the workout
        long numExercises = exerciseRepo.countByWorkoutId(workout.getId());

        if (numExercises == 0) {
            workoutsRepo.deleteById(workout.getId());
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

    static List<GroupedExerciseSets> groupExerciseSets(List<ExerciseModel> exerciseModels) {
        Map<Integer, List<ExerciseModel>> groupedExerciseModels = exerciseModels.stream()
                .collect(Collectors.groupingBy(ExerciseModel::getExerciseId));

        List<GroupedExerciseSets> groupedExerciseSets = new ArrayList<>();

        for (Map.Entry<Integer, List<ExerciseModel>> entry: groupedExerciseModels.entrySet()) {
            Integer exerciseId = entry.getKey();
            List<ScrubbedExerciseModel> sets = entry.getValue().stream()
                    .map(ScrubbedExerciseModel::fromExerciseModel).toList();

            groupedExerciseSets.add(new GroupedExerciseSets(exerciseId, sets));
        }
        System.out.println(groupedExerciseSets);
        return groupedExerciseSets;
    }
}
