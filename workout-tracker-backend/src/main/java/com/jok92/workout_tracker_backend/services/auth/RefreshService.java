package com.jok92.workout_tracker_backend.services.auth;

import com.jok92.workout_tracker_backend.models.auth.AccessRefreshPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RefreshService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private RedisService redisService;

    public AccessRefreshPair issueNewAccessRefreshPair(UUID userId) {
        return new AccessRefreshPair(jwtService.generateToken(userId), generateRefreshToken(userId));
    }

    public AccessRefreshPair refreshAccessToken(UUID refreshToken) {
        // Fetch userID from Redis
        String userString = redisService.get(refreshToken.toString())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        UUID userID = UUID.fromString(userString);

        // Generate new JWT access token with userId
        return new AccessRefreshPair(jwtService.generateToken(userID), regenerateRefreshToken(refreshToken, userID));
    }

    public UUID generateRefreshToken(UUID userID) {
        UUID token = UUID.randomUUID();
        long timeout = 7;
        TimeUnit unit = TimeUnit.DAYS;
        redisService.saveWithExpiry(token.toString(), userID.toString(), timeout, unit);
        return token;
    }

    private UUID regenerateRefreshToken(UUID oldRefreshToken, UUID userID) {
        redisService.delete(oldRefreshToken.toString());
        return generateRefreshToken(userID);
    }

    public boolean refreshTokenExists(String refreshToken) {
        return redisService.exists(refreshToken);
    }

    public void removeRefreshToken(String refreshToken) {
        redisService.delete(refreshToken);
    }
}
