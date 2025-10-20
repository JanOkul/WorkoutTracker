package com.jok92.workout_tracker_backend.controllers;

import com.jok92.workout_tracker_backend.models.JsonResponse.WorkoutExercisesResponse;
import com.jok92.workout_tracker_backend.models.auth.CustomUserDetail;
import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.ExerciseModel;
import com.jok92.workout_tracker_backend.models.workout.NewWorkouts;
import com.jok92.workout_tracker_backend.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workout")
public class DataController {

    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/{date}")
    public WorkoutExercisesResponse getWorkoutsForDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal CustomUserDetail principal
            ) {
        return new WorkoutExercisesResponse(dataService.getWorkoutsForDate(principal.getId(), date));
    }

    @PostMapping("/{date}")
    public List<ExerciseModel> addExercisesForDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal CustomUserDetail principal,
            @RequestBody NewWorkouts body) {


        return dataService.addExercisesForDate(principal.getId(), body);
    }
}
