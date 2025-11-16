package com.jok92.workout_tracker_backend.controllers;

import com.jok92.workout_tracker_backend.models.auth.*;
import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.UserModel;
import com.jok92.workout_tracker_backend.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.UUID;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Value("${jwt.expiryMins}")
    private int accessExpiryMins;

    @Value("${refresh.expiryDays}")
    private int refreshExpiryDays;

    @PostMapping("/signup")
    public UserModel signup(@RequestBody SignupDetails body) {
        return authService.signup(body);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDetails details) {

        AccessRefreshPair tokenPair = authService.login(details);
        String accessToken = tokenPair.accessToken();
        UUID refreshToken = tokenPair.refreshToken();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken.toString())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/api/auth/")
                .maxAge(Duration.ofDays(refreshExpiryDays))
                .build();

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/api/")
                .maxAge(Duration.ofMinutes(accessExpiryMins))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .body(new AccessTokenResponse(accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("refreshToken") String refreshToken) {
        authService.logout(refreshToken);
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/api/")
                .maxAge(Duration.ZERO)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/api/auth/")
                .maxAge(Duration.ZERO)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString()).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(value = "refreshToken") String cookie) {
        AccessRefreshPair tokenPair = authService.refresh(UUID.fromString(cookie));

        String accessToken = tokenPair.accessToken();
        UUID refreshToken = tokenPair.refreshToken();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken.toString())
                .httpOnly(false)
                .secure(false)
                .sameSite("Strict") //todo change
                .path("/api/auth/")
                .maxAge(Duration.ofDays(refreshExpiryDays))
                .build();

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(false)
                .secure(false)
                .sameSite("Strict")
                .path("/api/")
                .maxAge(Duration.ofMinutes(accessExpiryMins))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .body(new AccessTokenResponse(accessToken));
    }

    @GetMapping("/status")
    public StatusResponse status(@CookieValue(value = "refreshToken", required = false) String cookie) {
        if (cookie == null) {
            return new StatusResponse(false);
        }
        return new StatusResponse(authService.status(cookie));
    }

}
