package com.jok92.workout_tracker_backend.services;

import com.jok92.workout_tracker_backend.models.workout.Requests.stats.GetWorkouts;
import com.jok92.workout_tracker_backend.models.workout.Responses.stats.WorkoutEntry;
import com.jok92.workout_tracker_backend.models.workout.Responses.stats.WorkoutForExcercise;

import com.jok92.workout_tracker_backend.models.workout.Responses.workouts.GroupedSets;
import com.jok92.workout_tracker_backend.models.workout.Responses.workouts.ScrubbedSetModel;
import com.jok92.workout_tracker_backend.repositories.SetRepo;
import com.jok92.workout_tracker_backend.repositories.WorkoutsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatsService {

    @Autowired
    SetRepo setRepo;

    // Gets all the sets for an exercise over the last 90 days
    public List<WorkoutForExcercise> getWorkoutsByExercise(UUID userID, String exerciseName) {
        LocalDate startDate = LocalDate.now().minusDays(90);

        List<WorkoutEntry> sets = setRepo.findWorkoutsByUUIDandExerciseNameSinceStartDate(userID, exerciseName, startDate);

        Collection<List<WorkoutEntry>> groupedSets = sets.stream()
                .collect(Collectors.groupingBy(WorkoutEntry::getDateOfWorkout)).values();

        return groupedSets.stream().map(this::groupSetsByDate).toList();
    }

    WorkoutForExcercise groupSetsByDate(List<WorkoutEntry> entries) {
        return new WorkoutForExcercise(
                entries.getFirst().getDateOfWorkout(),
                entries.stream().map(
                        (set) ->
                                new ScrubbedSetModel(
                                        set.getSetNumber(),
                                        set.getWeight(),
                                        set.getReps()
                                )
                ).toList()
        );
    }
}
