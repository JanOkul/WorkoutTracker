export const updateTag = "UPDATE";
export const deleteTag = "DELETE";

export interface Set {
  setNumber: number;
  weight: number;
  reps: number;
}

export interface Exercise {
  exerciseId: string;
  sets: Set[];
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
