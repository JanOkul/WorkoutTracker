
-- The original passwords were:
-- user1: pass1
-- user2: pass2
-- user3: pass3
-- user1: pass4

INSERT INTO users(id, display_name, email, password, age, weight) VALUES
    ('b4820f69-b637-46fa-9152-7d63c18abe0a', 'user1', 'user1@example.com', '$2a$12$uTyVeSg6l95UIrtt7K3T1uttRoZNUdu/N5goMaue/GoZgoAuF35sq', 10, 100),
    ('cf2b3fac-e31e-4d75-a0e7-13cdd4a7f234', 'user2', 'user2@example.com', '$2a$12$win6YoZmxy0h7cO0cIkSQ.uBZK8P4aIm2mwJI2aPvlNHZmBwlaHPC', 20, 200),
    ('be1235cb-d464-48b9-8ce1-5cd37342ca3e', 'user3', 'user3@example.com', '$2a$12$FN0.vJ1WpfLKPSW1YNii7OC7qw2CdCRVutIMlO2gGQYIKyP3kT9ji', 30, 300),
    ('18ad1b84-ddb0-4aa7-9a1e-ee3358c9e578', 'user1', 'user4@example.com', '$2a$12$wfqBNVoNeVkejvHMtLiL5O4sSD8FRuxlfIKJpjTjcb21DO3S9fIBa', 40, 400);

INSERT INTO exercises VALUES
    ('squat', 'Lower body compound lift.'),
    ('bench press', 'Upper body pushing exercise.'),
    ('lat pulldown', 'Back pulling exercise.'),
    ('overhead press', 'Shoulder pushing exercise.');

INSERT INTO workouts (id, user_id, date_of_workout) VALUES
    -- User 1 Workouts
    (1, 'b4820f69-b637-46fa-9152-7d63c18abe0a', '2025-01-01'), -- Legs/Chest (E1, E2)
    (2, 'b4820f69-b637-46fa-9152-7d63c18abe0a', '2025-01-03'), -- Back/Shoulders (E3, E4)
    (3, 'b4820f69-b637-46fa-9152-7d63c18abe0a', '2025-01-05'), -- Full Body (E1, E3)

    -- User 2 Workouts
    (4, 'cf2b3fac-e31e-4d75-a0e7-13cdd4a7f234', '2025-01-01'), -- Chest/Shoulders (E2, E4)
    (5, 'cf2b3fac-e31e-4d75-a0e7-13cdd4a7f234', '2025-01-02'), -- Legs/Back (E1, E3)
    (6, 'cf2b3fac-e31e-4d75-a0e7-13cdd4a7f234', '2025-01-06'), -- Push (E2, E4)

    -- User 3 Workouts
    (7, 'be1235cb-d464-48b9-8ce1-5cd37342ca3e', '2025-01-06'), -- Strength (E1, E2)
    (8, 'be1235cb-d464-48b9-8ce1-5cd37342ca3e', '2025-01-07'), -- Volume (E3, E4)
    (9, 'be1235cb-d464-48b9-8ce1-5cd37342ca3e', '2025-01-10'), -- Light (E1, E3)

    -- User 4 Workouts
    (10, '18ad1b84-ddb0-4aa7-9a1e-ee3358c9e578', '2025-01-01'), -- Upper Body (E2, E4)
    (11, '18ad1b84-ddb0-4aa7-9a1e-ee3358c9e578', '2025-01-10'), -- Lower Body/Back (E1, E3)
    (12, '18ad1b84-ddb0-4aa7-9a1e-ee3358c9e578', '2025-01-12'); -- Push Day (E2, E4)


INSERT INTO sets (id, workout_id, exercise_id, set_number, weight, reps) VALUES
    -- Workout 1: Squat (E1), Bench Press (E2)
    (1, 1, 'squat', 1, 135, 8),
    (2, 1, 'squat', 2, 145, 6),
    (3, 1, 'bench press', 1, 60, 10),
    (4, 1, 'bench press', 2, 60, 5),

    -- Workout 2: Lat Pulldown (E3), Overhead Press (E4)
    (5, 2, 'lat pulldown', 1, 50, 12),
    (6, 2, 'lat pulldown', 2, 55, 10),
    (7, 2, 'overhead press', 1, 30, 8),
    (8, 2, 'overhead press', 2, 35, 6),

    -- Workout 3: Squat (E1), Lat Pulldown (E3)
    (9, 3, 'squat', 1, 100, 12),
    (10, 3, 'squat', 2, 100, 12),
    (11, 3, 'lat pulldown', 1, 40, 15),
    (12, 3, 'lat pulldown', 2, 40, 15),

    -- Workout 4: Bench Press (E2), Overhead Press (E4)
    (13, 4, 'bench press', 1, 90, 8),
    (14, 4, 'bench press', 2, 90, 8),
    (15, 4, 'overhead press', 1, 50, 10),
    (16, 4, 'overhead press', 2, 50, 10),

    -- Workout 5: Squat (E1), Lat Pulldown (E3)
    (17, 5, 'squat', 1, 120, 10),
    (18, 5, 'squat', 2, 130, 8),
    (19, 5, 'lat pulldown', 1, 60, 12),
    (20, 5, 'lat pulldown', 2, 60, 12),

    -- Workout 6: Bench Press (E2), Overhead Press (E4)
    (21, 6, 'bench press', 1, 100, 6),
    (22, 6, 'bench press', 2, 105, 5),
    (23, 6, 'overhead press', 1, 55, 8),
    (24, 6, 'overhead press', 2, 55, 8),

    -- Workout 7: Squat (E1), Bench Press (E2)
    (25, 7, 'squat', 1, 180, 5),
    (26, 7, 'squat', 2, 180, 5),
    (27, 7, 'bench press', 1, 120, 5),
    (28, 7, 'bench press', 2, 125, 3),

    -- Workout 8: Lat Pulldown (E3), Overhead Press (E4)
    (29, 8, 'lat pulldown', 1, 70, 10),
    (30, 8, 'lat pulldown', 2, 70, 10),
    (31, 8, 'overhead press', 1, 40, 10),
    (32, 8, 'overhead press', 2, 40, 10),

    -- Workout 9: Squat (E1), Lat Pulldown (E3)
    (33, 9, 'squat', 1, 90, 15),
    (34, 9, 'squat', 2, 90, 15),
    (35, 9, 'lat pulldown', 1, 30, 20),
    (36, 9, 'lat pulldown', 2, 30, 20),

    -- Workout 10: Bench Press (E2), Overhead Press (E4)
    (37, 10, 'bench press', 1, 75, 12),
    (38, 10, 'bench press', 2, 75, 10),
    (39, 10, 'overhead press', 1, 45, 12),
    (40, 10, 'overhead press', 2, 45, 12),

    -- Workout 11: Squat (E1), Lat Pulldown (E3)
    (41, 11, 'squat', 1, 150, 6),
    (42, 11, 'squat', 2, 150, 6),
    (43, 11, 'lat pulldown', 1, 65, 8),
    (44, 11, 'lat pulldown', 2, 65, 8),

    -- Workout 12: Bench Press (E2), Overhead Press (E4)
    (45, 12, 'bench press', 1, 85, 10),
    (46, 12, 'bench press', 2, 85, 10),
    (47, 12, 'overhead press', 1, 38, 10),
    (48, 12, 'overhead press', 2, 38, 10);
