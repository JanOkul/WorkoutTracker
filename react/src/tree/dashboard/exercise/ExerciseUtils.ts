import apiAxios from "@/api/axiosInterceptor";
import type { Dispatch, SetStateAction } from "react";
import type { SingleSet } from "../log/LogUtils";

export interface workoutEntryISOString {
  dateOfWorkout: string;
  sets: SingleSet[];
}

export interface workoutEntryDate {
  date: Date;
  sets: SingleSet[];
}

export function epley1RM(weight: number, reps: number): number {
  return weight * (1 + reps / 30);
}

export function aggregateExerciseData(
  data: workoutEntryDate[],
  startDate: number
) {
  const aggergatedData = data.map((entry) => ({
    date: entry.date.getTime(),
    avg:
      entry.sets.reduce((acc, val) => acc + epley1RM(val.weight, val.reps), 0) /
      entry.sets.length,
    max: entry.sets.reduce(
      (acc, val) => Math.max(acc, epley1RM(val.weight, val.reps)),
      -Infinity
    ),
  }));

  return aggergatedData.filter((entry) => {
    const start = new Date().getTime() - startDate * 24 * 60 * 60 * 1000;
    return entry.date >= start;
  });
}

export async function getExerciseData(
  exerciseName: string,
  setData: Dispatch<SetStateAction<workoutEntryDate[]>>
) {
  const apiUrl = "/api/stats/get-workouts-by-exercise";
  try {
    const response = await apiAxios.get<workoutEntryISOString[]>(apiUrl, {
      params: {
        exerciseName: exerciseName,
      },
    });

    const data = response.data.map(
      (entry) =>
        ({
          date: new Date(entry.dateOfWorkout),
          sets: entry.sets,
        } as workoutEntryDate)
    );

    data.sort((a, b) => {
      return a.date.getTime() - b.date.getTime();
    });

    setData(data);
  } catch (err) {
    setData([]);
  }
}
