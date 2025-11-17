package com.jok92.workout_tracker_backend.services;

import com.jok92.workout_tracker_backend.exceptions.UserNotFoundException;
import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.UserModel;
import com.jok92.workout_tracker_backend.models.workout.Responses.misc.DisplayName;
import com.jok92.workout_tracker_backend.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MiscService {
    @Autowired
    UserRepo userRepo;
    public DisplayName getDisplayName(UUID userId) {
        UserModel user = userRepo.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return new DisplayName(user.getDisplayName());
    }
}
