package com.jok92.workout_tracker_backend.controllers;

import com.jok92.workout_tracker_backend.models.auth.LoginDetails;
import com.jok92.workout_tracker_backend.models.auth.SignupDetails;
import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.UserModel;
import com.jok92.workout_tracker_backend.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


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
    public ResponseEntity<?> login(@RequestBody LoginDetails details, HttpServletResponse response) {
        String jwt = authService.login(details);
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        response.addCookie(cookie);
        return ResponseEntity.ok(Map.of("message", "Login successful", "jwt", jwt));
    }
}
