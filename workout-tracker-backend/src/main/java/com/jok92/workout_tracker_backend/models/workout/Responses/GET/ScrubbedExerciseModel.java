package com.jok92.workout_tracker_backend.models.workout.Responses.GET;

import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.ExerciseModel;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * A mirror of the {@link ExerciseModel} class, but with all database IDs scrubbed.
 * Used for responding to a client in controllers to not reveal private database information.
 */
public class ScrubbedExerciseModel {
    private Integer exerciseId;
    private Integer setNumber;
    private Integer weight;
    private Integer reps;

    /**
     * Creates a new instance of the class from a standard {@link ExerciseModel} class.
     * @param model A standard {@link ExerciseModel} class.
     * @return A {@link ScrubbedExerciseModel} instance.
     */
    public static ScrubbedExerciseModel fromExerciseModel(ExerciseModel model) {
        return new ScrubbedExerciseModel(
                model.getExerciseId(),
                model.getSetNumber(),
                model.getWeight(),
                model.getReps()
        );
    }
}
