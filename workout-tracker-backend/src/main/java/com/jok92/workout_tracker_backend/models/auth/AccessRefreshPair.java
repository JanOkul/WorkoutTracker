package com.jok92.workout_tracker_backend.models.auth;

import java.util.UUID;

public record AccessRefreshPair(
        String jwtToken,
        UUID refreshToken
) {}
