package com.jok92.workout_tracker_backend.services;

import com.jok92.workout_tracker_backend.exceptions.SetNotFoundException;
import com.jok92.workout_tracker_backend.exceptions.WorkoutNotFoundException;
import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.SetModel;
import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.WorkoutsModel;
import com.jok92.workout_tracker_backend.models.workout.Requests.DeleteSet;
import com.jok92.workout_tracker_backend.models.workout.Requests.NewSet;
import com.jok92.workout_tracker_backend.models.workout.Requests.UpdateSet;
import com.jok92.workout_tracker_backend.models.workout.Responses.workouts.DataGetResponse;
import com.jok92.workout_tracker_backend.models.workout.Responses.workouts.GroupedSets;
import com.jok92.workout_tracker_backend.models.workout.Responses.workouts.ScrubbedSetModel;
import com.jok92.workout_tracker_backend.repositories.SetRepo;
import com.jok92.workout_tracker_backend.repositories.UserRepo;
import com.jok92.workout_tracker_backend.repositories.WorkoutsRepo;
import jakarta.persistence.EntityManager;
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
    @Autowired
    UserRepo userRepo;
    @Autowired
    SetRepo setRepo;
    @Autowired
    WorkoutsRepo workoutsRepo;


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

        List<SetModel> setModels = setRepo.findAllByWorkoutId(targetWorkoutModel.get().getId());
        // Remove all the database identifiers from the response.
        return new DataGetResponse(
                date,
                groupExerciseSets(setModels)
        );
    }

    /**
     * Adds single or multiple exercises for a single exercise type.
     * @param userId The target user's UUID
     * @param date The workout that the exercises should be added to.
     * @param newSet Data from the client on when and what exercises to add.
     * @return The exercise added to the database.
     */
    @Transactional
    public ScrubbedSetModel addExercisesForDate(UUID userId, LocalDate date, NewSet newSet) {
        verifyDate(date);

        // Check if workout exists. If empty create new workout.
        WorkoutsModel workout = workoutsRepo.findByUserIdAndDateOfWorkout(userId, date)
                .orElseGet(() -> workoutsRepo.save(new WorkoutsModel(null, userId, date)));

        SetModel setModel = new SetModel(
                                null,
                                workout.getId(),
                                newSet.getExerciseId(),
                                newSet.getSetNumber(),
                                newSet.getWeight(),
                                newSet.getReps()
        );

        SetModel dbResponse = setRepo.save(setModel);

        // Remove database information from database response
        return ScrubbedSetModel.fromExerciseModel(dbResponse);

    }


    /**
     * Changes a single exercise entry.
     *
     * @param userId         The target user's UUID
     * @param date           The date of the workout the target exercise is in
     * @param updateSet What to replace the existing exercise with.
     * @return The new exercise entry.
     */
    @Transactional
    public UpdateSet changeExerciseForDate(UUID userId, LocalDate date, UpdateSet updateSet) {
        verifyDate(date);

        // Check that the workout exists
        WorkoutsModel workout = workoutsRepo.findByUserIdAndDateOfWorkout(userId, date)
                .orElseThrow(WorkoutNotFoundException::new);

        // Check that the set exists
        SetModel set = setRepo.findByWorkoutIdAndExerciseIdAndSetNumber(
                workout.getId(), updateSet.getExerciseId(), updateSet.getSetNumber())
                .orElseThrow(SetNotFoundException::new);

        set.setExerciseId(updateSet.getExerciseId());
        set.setSetNumber(updateSet.getSetNumber());
        set.setWeight(updateSet.getWeight());
        set.setReps(updateSet.getReps());

        return new UpdateSet(
                set.getExerciseId(),
                set.getSetNumber(),
                set.getWeight(),
                set.getReps()
        );
    }


    /**
     * Deletes an exercise from a workout, if the workout has no more exercises, it is also deleted.
     * @param userId The target user's UUID
     * @param date The date the exercise to be removed is on
     * @param deleteSet Target exercise details
     */
    @Transactional
    public void deleteExerciseForDate(UUID userId, LocalDate date, DeleteSet deleteSet) {
        verifyDate(date);

        WorkoutsModel workout = workoutsRepo.findByUserIdAndDateOfWorkout(userId, date)
                .orElseThrow(WorkoutNotFoundException::new);

        setRepo.deleteByWorkoutIdAndSetNumber(workout.getId(), deleteSet.getSetNumber());

        // If there are no more exercises corresponding to a workout, remove the workout
        long numExercises = setRepo.countByWorkoutId(workout.getId());

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

    static List<GroupedSets> groupExerciseSets(List<SetModel> setModels) {
        Map<Long, List<SetModel>> groupedExerciseModels = setModels.stream()
                .collect(Collectors.groupingBy(SetModel::getExerciseId));

        List<GroupedSets> groupedSets = new ArrayList<>();

        for (Map.Entry<Long, List<SetModel>> entry: groupedExerciseModels.entrySet()) {
            Long exerciseId = entry.getKey();
            List<ScrubbedSetModel> sets = entry.getValue().stream()
                    .map(ScrubbedSetModel::fromExerciseModel).toList();

            groupedSets.add(new GroupedSets(exerciseId, sets));
        }
        System.out.println(groupedSets);
        return groupedSets;
    }
}
