export const updateTag = "UPDATE";
export const deleteTag = "DELETE";

export interface SingleSet {
  setNumber: number;
  weight: number;
  reps: number;
}

export interface Exercise {
  exerciseId: string;
  sets: SingleSet[];
}

export interface Workout {
  dateOfWorkout: string;
  exercises: Exercise[];
}

export function formatDate(date: Date): String {
  return date.toLocaleDateString("en-US", {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  });
}

export function updateSet(
  setWorkout: React.Dispatch<React.SetStateAction<Workout>>,
  exerciseId: string | null,
  setNumber: number,
  weight: number,
  reps: number
) {
  setWorkout((prev) => {
    if (!prev) return prev;

    return {
      ...prev,
      exercises: prev.exercises.map((exercise) =>
        exercise.exerciseId === exerciseId
          ? {
              ...exercise,
              sets: exercise.sets.map((set) =>
                set.setNumber === setNumber ? { ...set, weight, reps } : set
              ),
            }
          : exercise
      ),
    };
  });
}

export function deleteSet(
  setWorkout: React.Dispatch<React.SetStateAction<Workout>>,
  exerciseId: string,
  setNumber: number
) {
  setWorkout((prev) => {
    if (!prev) return prev;

    return {
      ...prev,
      exercises: prev.exercises
        .map((exercise) =>
          exercise.exerciseId === exerciseId
            ? {
                ...exercise,
                sets: exercise.sets.filter(
                  (set) => set.setNumber !== setNumber
                ),
              }
            : exercise
        )
        .filter((exercise) => exercise.sets.length > 0),
    };
  });
}

// Creates a new Exercise entry into the current workout
export function createExercise(
  setWorkout: React.Dispatch<React.SetStateAction<Workout>>,
  value: string
) {
  const name = value.toLowerCase();

  if (!name) {
    return;
  }

  setWorkout((prev: Workout) => {
    if (name.length === 0) {
      //todo add exerciselist comparison
      console.error("Invalid Exercise ID provided.");
      alert("Please enter a valid, positive number for the Exercise ID.");
      return prev;
    }

    const idAlreadyExists = prev.exercises.some((ex) => ex.exerciseId === name);

    if (idAlreadyExists) {
      console.error(`Exercise Name ${name} already exists.`);

      alert(
        `Exercise ID ${name} is already present. Please use a unique name.`
      );
      return prev;
    }

    const newExercise = {
      exerciseId: name,
      sets: [],
    };

    return {
      ...prev,
      exercises: [...prev.exercises, newExercise],
    };
  });
}
