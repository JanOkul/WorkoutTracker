package com.jok92.workout_tracker_backend.models.workout.Responses.workouts;

import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.SetModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * A mirror of the {@link SetModel} class, but with all database IDs scrubbed.
 * Used for responding to a client in controllers to not reveal private database information.
 */
public class ScrubbedSetModel {
    private Short setNumber;
    private Integer weight;
    private Short reps;

    /**
     * Creates a new instance of the class from a standard {@link SetModel} class.
     * @param model A standard {@link SetModel} class.
     * @return A {@link ScrubbedSetModel} instance.
     */
    public static ScrubbedSetModel fromExerciseModel(SetModel model) {
        return new ScrubbedSetModel(
                model.getSetNumber(),
                model.getWeight(),
                model.getReps()
        );
    }
}
