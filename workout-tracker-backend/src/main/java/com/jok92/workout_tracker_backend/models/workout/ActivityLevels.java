package com.jok92.workout_tracker_backend.models.workout;

public enum ActivityLevels {
    SEDENTARY,
    LIGHTLY_ACTIVE,
    MODERATELY_ACTIVE,
    VERY_ACTIVE;

    public static ActivityLevels fromString(String value) {
        if (value == null) return null;
        return ActivityLevels.valueOf(value);
    }
}
