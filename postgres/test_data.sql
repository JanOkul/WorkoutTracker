-- 1. Insert a few users
INSERT INTO users (display_name, email, password, age, weight) VALUES
('fit_fred', 'fred@example.com', 'hashed_pass_1', 30, 80),
('active_ana', 'ana@example.com', 'hashed_pass_2', 24, 62);

---

-- 2. Insert workouts for the users
-- We'll use subqueries to get the user_id's to maintain foreign key integrity.
INSERT INTO workouts (user_id, date_of_workout) VALUES
((SELECT id FROM users WHERE display_name = 'fit_fred'), '2025-10-15'),
((SELECT id FROM users WHERE display_name = 'active_ana'), '2025-10-16'),
((SELECT id FROM users WHERE display_name = 'fit_fred'), '2025-10-16');

---

-- 3. Insert exercises for the workouts
-- We'll use subqueries to get the workout_id's.
-- The exercise_id is just an arbitrary external ID (like a reference to an exercise definition).
INSERT INTO exercises (workout_id, exercise_id, set_number, weight, reps) VALUES
-- Exercises for fit_fred's first workout (ID 1)
((SELECT id FROM workouts WHERE user_id = (SELECT id FROM users WHERE display_name = 'fit_fred') AND date_of_workout = '2025-10-15' LIMIT 1), 101, 1, 60000, 10), -- Bench Press
((SELECT id FROM workouts WHERE user_id = (SELECT id FROM users WHERE display_name = 'fit_fred') AND date_of_workout = '2025-10-15' LIMIT 1), 101, 2, 60000, 8),
((SELECT id FROM workouts WHERE user_id = (SELECT id FROM users WHERE display_name = 'fit_fred') AND date_of_workout = '2025-10-15' LIMIT 1), 205, 1, 15000, 12), -- Dumbbell Curls

-- Exercises for active_ana's workout (ID 2)
((SELECT id FROM workouts WHERE user_id = (SELECT id FROM users WHERE display_name = 'active_ana') AND date_of_workout = '2025-10-16' LIMIT 1), 302, 1, NULL, 50), -- Bodyweight Squats (no weight)
((SELECT id FROM workouts WHERE user_id = (SELECT id FROM users WHERE display_name = 'active_ana') AND date_of_workout = '2025-10-16' LIMIT 1), 302, 2, NULL, 50);
