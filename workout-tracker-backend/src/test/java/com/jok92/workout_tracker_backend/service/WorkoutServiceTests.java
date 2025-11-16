package com.jok92.workout_tracker_backend.service;

import com.jok92.workout_tracker_backend.TestContainersInit;
import com.jok92.workout_tracker_backend.models.workout.Responses.workouts.DataGetResponse;
import com.jok92.workout_tracker_backend.models.workout.Responses.workouts.GroupedSets;
import com.jok92.workout_tracker_backend.models.workout.Responses.workouts.ScrubbedSetModel;
import com.jok92.workout_tracker_backend.repositories.SetRepo;
import com.jok92.workout_tracker_backend.repositories.UserRepo;
import com.jok92.workout_tracker_backend.repositories.WorkoutsRepo;
import com.jok92.workout_tracker_backend.services.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

//todo get tests working
public class WorkoutServiceTests extends TestContainersInit {
    @Mock
    private UserRepo userRepo;

    @Mock
    private WorkoutsRepo workoutsRepo;

    @Mock
    private SetRepo setRepo;

    @InjectMocks
    private WorkoutService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        System.out.println("POSTGRES URL " + postgres.getJdbcUrl());
    }

    @Test
    public void getWorkoutsForDateTest() {
        LocalDate date = LocalDate.parse("2025-01-01");
        UUID id = UUID.fromString("b4820f69-b637-46fa-9152-7d63c18abe0a");
        GroupedSets[] sets = {
            new GroupedSets(
                    (long) 1,
                    Arrays.asList(
                            new ScrubbedSetModel((short) 1, 135, (short) 8),
                            new ScrubbedSetModel((short) 2, 145, (short) 6))),
            new GroupedSets(
                    (long) 2,
                    Arrays.asList(
                            new ScrubbedSetModel((short) 1, 60, (short) 10),
                            new ScrubbedSetModel((short) 2, 60, (short) 5)))
        };

        DataGetResponse expected = new DataGetResponse(
                date,
                Arrays.asList(sets)
        );

        DataGetResponse actual = service.getWorkoutsForDate(id, date);

        assertEquals(expected, actual);
    }
}
