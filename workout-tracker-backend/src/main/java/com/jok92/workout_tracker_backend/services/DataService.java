package com.jok92.workout_tracker_backend.services;

import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.ExerciseModel;
import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.WorkoutModel;
import com.jok92.workout_tracker_backend.models.workout.NewExercise;
import com.jok92.workout_tracker_backend.models.workout.NewWorkouts;
import com.jok92.workout_tracker_backend.repositories.ExerciseRepo;
import com.jok92.workout_tracker_backend.repositories.UserRepo;
import com.jok92.workout_tracker_backend.repositories.WorkoutRepo;
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
    @Autowired
    UserRepo userRepo;

    @Autowired
    ExerciseRepo exerciseRepo;

    @Autowired
    WorkoutRepo workoutRepo;

    @Autowired
    public DataService(UserRepo userRepo, WorkoutRepo workoutRepo, ExerciseRepo exerciseRepo) {
        this.userRepo = userRepo;
        this.exerciseRepo = exerciseRepo;
        this.workoutRepo = workoutRepo;
    }

    public List<ExerciseModel> getWorkoutsForDate(UUID userId, LocalDate date) {
        verifyDate(date);

        Optional<WorkoutModel> targetWorkoutModel = workoutRepo.findByUserIdAndDateOfWorkout(userId, date);

        if (targetWorkoutModel.isEmpty()) {
            return new ArrayList<>();
        }

        return exerciseRepo.findAllByWorkoutId(targetWorkoutModel.get().getId());
    }

    public List<ExerciseModel> addExercisesForDate(UUID userId, NewWorkouts newWorkout) {
        LocalDate date = newWorkout.getDateOfWorkout();

        // Check if workout exists. If empty create new workout.
        WorkoutModel workout = workoutRepo.findByUserIdAndDateOfWorkout(
                userId, date).orElseGet(() -> workoutRepo.save(new WorkoutModel(null, userId, date)));;


        List<NewExercise> newExercises = newWorkout.getNewExercises();
        List<ExerciseModel> exerciseModels = new ArrayList<>();

        for (NewExercise exercise: newExercises) {
            exerciseModels.add(
                    new ExerciseModel(
                            null,
                            workout.getId(),
                            newWorkout.getExerciseId(),
                            exercise.getSetNumber(),
                            exercise.getWeight(),
                            exercise.getReps()
                    )
            );
        }

        return exerciseRepo.saveAll(exerciseModels);

    }

    static void verifyDate(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Time travellers must be patient"
            );
        }
    }
}
