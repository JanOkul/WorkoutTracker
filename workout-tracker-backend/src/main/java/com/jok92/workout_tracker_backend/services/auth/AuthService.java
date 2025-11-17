package com.jok92.workout_tracker_backend.services.auth;

import com.jok92.workout_tracker_backend.models.auth.AccessRefreshPair;
import com.jok92.workout_tracker_backend.models.auth.LoginDetails;
import com.jok92.workout_tracker_backend.models.auth.SignupDetails;
import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.UserModel;
import com.jok92.workout_tracker_backend.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshService refreshService;
    final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    /**
     * @param user The user's sign up details sent by the client.
     * @return The row returned by entering a value into the database
     */
    public UserModel signup(SignupDetails user) {
        Optional<UserModel> existingUser = userRepo.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserModel model = new UserModel(user.getDisplayName(), user.getEmail(),
                user.getPassword(), user.getAge(), user.getWeight());

        return userRepo.save(model);
    }

    /**
     * @param details Email and password details sent by the client.
     * @return A JWT token with the user's UUID.
     */
    public AccessRefreshPair login(LoginDetails details) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(details.getEmail(), details.getPassword())
        );

        UserModel userId = userRepo.findByEmail(details.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        System.out.println(userId.getId().toString());
        return refreshService.issueNewAccessRefreshPair(userId.getId());
    }

    public void logout(String refreshToken) {
        refreshService.removeRefreshToken(refreshToken);
    }

    public AccessRefreshPair refresh(UUID refreshToken) {
        return refreshService.refreshAccessToken(refreshToken);
    }

    /**
     * Allows a client to check if they're logged in with an http only refresh token.
     * @param refreshToken UUID token to be verified
     * @return If the token exists as a key in the database.
     */
    public boolean status(String refreshToken) {
        return refreshService.refreshTokenExists(refreshToken);
    }
}
