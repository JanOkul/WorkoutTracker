package com.jok92.workout_tracker_backend.controllers;

import com.jok92.workout_tracker_backend.models.workout.Requests.UpdateExercise;
import com.jok92.workout_tracker_backend.models.workout.Responses.GET.DataGetResponse;
import com.jok92.workout_tracker_backend.models.auth.CustomUserDetail;
import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.ExerciseModel;
import com.jok92.workout_tracker_backend.models.workout.Requests.DeleteExercise;
import com.jok92.workout_tracker_backend.models.workout.Requests.NewWorkouts;
import com.jok92.workout_tracker_backend.models.workout.Responses.GET.ScrubbedExerciseModel;
import com.jok92.workout_tracker_backend.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/workout")
public class DataController {

    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    /**
     * Retrieves ALL the exercises for a user on a given date, if there is a workout present
     * @param date The date to retrieve exercises on
     * @param principal Decoded JWT token with userID.
     * @return A JSON response with all the exercises as a list of exercises.
     */
    @GetMapping("/{date}")
    public DataGetResponse getWorkoutsForDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal CustomUserDetail principal
            ) {

        return new DataGetResponse(
                date,
                dataService.getWorkoutsForDate(principal.getId(), date)
        );
    }

    /**
     * Adds a list of exercises to a workout.
     * @param date The date the workout was on, retrieved from the URL
     * @param principal Decoded JWT token with userID.
     * @param body Request body
     * @return A JSON response with all the exercises added.
     */
    @PostMapping("/{date}")
    public
    ResponseEntity<List<ScrubbedExerciseModel>> addExercisesForDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal CustomUserDetail principal,
            @RequestBody NewWorkouts body) {

        return ResponseEntity
                .created(URI.create("/"+date))
                .body(dataService.addExercisesForDate(principal.getId(), date, body));
    }

    /**
     * Updates a single exercise entry.
     * @param date The date the workout was on, retrieved from the URL
     * @param principal Decoded JWT token with userID.
     * @param body Request body
     * @return A JSON response with the modified exercise entry.
     */
    @PutMapping("/{date}")
    public ScrubbedExerciseModel changeExerciseForDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal CustomUserDetail principal,
            @RequestBody UpdateExercise body) {

        return dataService.changeExerciseForDate(principal.getId(), date, body);
    }

    /**
     * Removes a single entry from a workout.
     * @param date The date the workout was on, retrieved from the URL
     * @param principal Decoded JWT token with userID.
     * @param body Request body
     */
    @DeleteMapping("/{date}")
    public void deleteExerciseForDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal CustomUserDetail principal,
            @RequestBody DeleteExercise body
            ) {

        dataService.deleteExerciseForDate(principal.getId(), date, body);
    }
}
