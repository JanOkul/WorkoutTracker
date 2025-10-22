package com.jok92.workout_tracker_backend.controllers;

import com.jok92.workout_tracker_backend.models.auth.AccessRefreshPair;
import com.jok92.workout_tracker_backend.models.auth.AccessTokenResponse;
import com.jok92.workout_tracker_backend.models.auth.LoginDetails;
import com.jok92.workout_tracker_backend.models.auth.SignupDetails;
import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.UserModel;
import com.jok92.workout_tracker_backend.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public UserModel signup(@RequestBody SignupDetails body) {
        return authService.signup(body);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDetails details) {
        AccessRefreshPair tokenPair = authService.login(details);

        String accessToken = tokenPair.jwtToken();
        UUID refreshToken = tokenPair.refreshToken();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken.toString())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new AccessTokenResponse(accessToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("refresh_token") String cookie) {
        AccessRefreshPair tokenPair = authService.refresh(UUID.fromString(cookie));

        String accessToken = tokenPair.jwtToken();
        UUID refreshToken = tokenPair.refreshToken();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken.toString())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new AccessTokenResponse(accessToken));
    }

}
