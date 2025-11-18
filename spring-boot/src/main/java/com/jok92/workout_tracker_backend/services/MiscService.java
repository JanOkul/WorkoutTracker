package com.jok92.workout_tracker_backend.services;

import com.jok92.workout_tracker_backend.exceptions.UserNotFoundException;
import com.jok92.workout_tracker_backend.models.workout.DatabaseModels.UserModel;
import com.jok92.workout_tracker_backend.models.workout.Responses.misc.DisplayName;
import com.jok92.workout_tracker_backend.models.workout.Responses.misc.GetExList;
import com.jok92.workout_tracker_backend.repositories.ExerciseRepo;
import com.jok92.workout_tracker_backend.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MiscService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    ExerciseRepo exerciseRepo;

    public DisplayName getDisplayName(UUID userId) {
        UserModel user = userRepo.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return new DisplayName(user.getDisplayName());
    }

    public GetExList getExList() {
        return new GetExList(exerciseRepo.findAll());
    }
}
