package com.jok92.workout_tracker_backend.services.auth;

import com.jok92.workout_tracker_backend.exceptions.UserNotFoundException;
import com.jok92.workout_tracker_backend.models.auth.CustomUserDetail;
import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.UserModel;
import com.jok92.workout_tracker_backend.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = repo.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return new CustomUserDetail(user);
    }

    public UserDetails loadUserById(UUID id) throws UsernameNotFoundException {
        UserModel user = repo.findById(id).orElseThrow(UserNotFoundException::new);
        return new CustomUserDetail(user);
    }
}
