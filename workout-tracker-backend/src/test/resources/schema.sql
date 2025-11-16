
CREATE TABLE IF NOT EXISTS users (
	-- Admin content
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	display_name varchar(20) NOT NULL,
	email varchar(50) UNIQUE NOT NULL,
	password varchar(100) NOT NULL,
	created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
	-- Fitness content
	age int2 NOT NULL,
	weight int4 NOT NULL
);

CREATE TABLE IF NOT EXISTS workouts (
	id BIGSERIAL PRIMARY KEY,
	user_id UUID NOT NULL,
	date_of_workout date NOT NULL,
	
	CONSTRAINT fk_user 
		FOREIGN KEY (user_id) 
		REFERENCES USERS(id) 
		ON DELETE CASCADE,
		
	CONSTRAINT one_workout_per_day_per_user 
		UNIQUE(user_id, date_of_workout)
);
	
CREATE TABLE IF NOT EXISTS sets (
	-- Admin content
	id BIGSERIAL PRIMARY KEY,
	workout_id BIGINT NOT NULL,
	exercise_id BIGSERIAL NOT NULL,
	-- Fitness content
	set_number int2 NOT NULL,
	weight int4,
	reps int2,
	
	CONSTRAINT fk_workout
		FOREIGN KEY (workout_id) 
		REFERENCES WORKOUTS(id) 
		ON DELETE CASCADE,
	
	CONSTRAINT fk_exercise
		FOREIGN KEY (exercise_id)
		REFERENCES exercises(id)
		ON DELETE SET NULL,
	
	
	CONSTRAINT unique_exercise_sets
		UNIQUE(workout_id, exercise_id, set_number)
		
);

CREATE TABLE IF NOT EXISTS exercises (
	id BIGSERIAL PRIMARY KEY,
	name varchar(40) NOT NULL,
	description varchar(200)
);

	
