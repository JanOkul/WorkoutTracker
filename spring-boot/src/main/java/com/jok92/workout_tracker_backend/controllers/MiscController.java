package com.jok92.workout_tracker_backend.controllers;

import com.jok92.workout_tracker_backend.models.auth.CustomUserDetail;
import com.jok92.workout_tracker_backend.models.workout.Responses.misc.DisplayName;
import com.jok92.workout_tracker_backend.services.MiscService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/misc")
public class MiscController {
    @Autowired
    MiscService miscService;

    @GetMapping("/get-display-name")
    public DisplayName getDisplayName(@AuthenticationPrincipal CustomUserDetail principal) {
        System.out.println("misc controller");
        System.out.println(principal.getId());
        return miscService.getDisplayName(principal.getId());
    }
}
