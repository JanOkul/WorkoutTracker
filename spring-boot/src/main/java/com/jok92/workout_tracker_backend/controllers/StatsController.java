package com.jok92.workout_tracker_backend.controllers;

import com.jok92.workout_tracker_backend.models.auth.CustomUserDetail;
import com.jok92.workout_tracker_backend.models.workout.Requests.stats.GetWorkouts;
import com.jok92.workout_tracker_backend.models.workout.Responses.stats.WorkoutEntry;
import com.jok92.workout_tracker_backend.models.workout.Responses.stats.WorkoutForExcercise;
import com.jok92.workout_tracker_backend.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    StatsService statsService;

    @GetMapping("/get-workouts-by-exercise")
    public List<WorkoutForExcercise> getWorkoutsByExercise(@AuthenticationPrincipal CustomUserDetail principal, @RequestParam String exerciseName) {
        System.out.println("called");
        System.out.println(exerciseName);
        return statsService.getWorkoutsByExercise(principal.getId(), exerciseName);
    }

}
