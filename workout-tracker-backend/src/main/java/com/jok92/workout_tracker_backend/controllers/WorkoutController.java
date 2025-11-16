package com.jok92.workout_tracker_backend.controllers;

import com.jok92.workout_tracker_backend.models.workout.Requests.NewSet;
import com.jok92.workout_tracker_backend.models.workout.Requests.UpdateSet;
import com.jok92.workout_tracker_backend.models.workout.Responses.workouts.DataGetResponse;
import com.jok92.workout_tracker_backend.models.auth.CustomUserDetail;
import com.jok92.workout_tracker_backend.models.workout.Requests.DeleteSet;
import com.jok92.workout_tracker_backend.models.workout.Responses.workouts.ScrubbedSetModel;
import com.jok92.workout_tracker_backend.services.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/workout")
public class WorkoutController {

    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
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

        return workoutService.getWorkoutsForDate(principal.getId(), date);

    }

    /**
     * Adds a list of exercises to a workout.
     * @param date The date the workout was on, retrieved from the URL
     * @param principal Decoded JWT token with userID.
     * @param body Request body
     * @return A JSON response with all the exercises added.
     */
    @PostMapping("/{date}")
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<ScrubbedSetModel> addExercisesForDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal CustomUserDetail principal,
            @RequestBody NewSet body) {

        return ResponseEntity
                .created(URI.create("/"+date))
                .body(workoutService.addExercisesForDate(principal.getId(), date, body));
    }

    /**
     * Updates a single exercise entry.
     * @param date The date the workout was on, retrieved from the URL
     * @param principal Decoded JWT token with userID.
     * @param body Request body
     * @return A JSON response with the modified exercise entry.
     */
    @PutMapping("/{date}")
    public UpdateSet changeExerciseForDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal CustomUserDetail principal,
            @RequestBody UpdateSet body) {
        System.out.println(body);
        return workoutService.changeExerciseForDate(principal.getId(), date, body);
    }

    /**
     * Removes a single entry from a workout.
     * @param date The date the workout was on, retrieved from the URL
     * @param principal Decoded JWT token with userID.
     * @param body Request body
     */
    @DeleteMapping("/{date}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExerciseForDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal CustomUserDetail principal,
            @RequestBody DeleteSet body
            ) {

        workoutService.deleteExerciseForDate(principal.getId(), date, body);
    }
}
