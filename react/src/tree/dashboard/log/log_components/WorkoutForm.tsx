import { Button } from "@/components/ui/button";

import apiAxios from "@/api/axiosInterceptor";
import {
  Empty,
  EmptyDescription,
  EmptyHeader,
  EmptyMedia,
  EmptyTitle,
} from "@/components/ui/empty";
import { capitalizeWords } from "@/global/utils";
import { Ghost } from "lucide-react";
import type { SetStateAction } from "react";
import { Link } from "react-router-dom";
import { logUrl } from "../Log";
import {
  deleteSet,
  formatDate,
  type SingleSet,
  type Workout,
} from "../LogUtils";
import ExerciseForm from "./ExerciseForm";
import NewWorkout from "./NewWorkout";

interface workoutProp {
  setWorkout: (value: SetStateAction<Workout>) => void;
  date: string;
  workout: Workout;
}

const WorkoutForm = ({ date, setWorkout, workout }: workoutProp) => {
  const dateApiUrl = `${logUrl}${date}`;

  async function addSet(exerciseId: string) {
    const targetExercise = workout.exercises.find(
      (ex) => ex.exerciseId === exerciseId
    );

    const newSetNumber =
      targetExercise && targetExercise.sets.length > 0
        ? Math.max(...targetExercise.sets.map((s) => s.setNumber)) + 1
        : 1;

    setWorkout((prev) => ({
      ...prev,
      exercises: prev.exercises.map((ex) =>
        ex.exerciseId === exerciseId
          ? {
              ...ex,
              sets: [
                ...ex.sets,
                { setNumber: newSetNumber, weight: 0, reps: 0 },
              ],
            }
          : ex
      ),
    }));

    try {
      const response = await apiAxios.post(dateApiUrl, {
        exerciseId,
        setNumber: newSetNumber,
        weight: 0,
        reps: 0,
      });
    } catch (err) {
      console.error("Failed to add new set:", err);
      deleteSet(setWorkout, exerciseId, newSetNumber);
    }
  }

  return (
    <div className="border-2 rounded-lg p-2 pb-4 pt-4">
      <h2 className="text-center ml-4 mr-4">
        {formatDate(new Date(workout.dateOfWorkout))}
      </h2>

      {/* For each exercise in the workout*/}
      <div className="flex1 justify-center">
        {workout.exercises.length > 0 ? (
          workout.exercises.map((ex) => (
            <div key={ex.exerciseId} className="m-4">
              <Link
                to={"/dashboard/exercises/" + ex.exerciseId}
                className="w-auto"
                state={{ fromLogs: true }}
              >
                <Button variant="link" className="cursor-pointer pl-0">
                  <h3>{capitalizeWords(ex.exerciseId)}</h3>
                </Button>
              </Link>
              {/* For each set in an exercise*/}
              <div className="flex space-x-3">
                <h5 className="w-1/2">Weight</h5>

                <h5 className="w-1/2">Reps</h5>
                <Button onClick={() => {}} className="opacity-0 h-7" />
                <Button onClick={() => {}} className="opacity-0 h-7 mr-0.5" />
              </div>
              {ex.sets
                .sort((a: SingleSet, b: SingleSet) => a.setNumber - b.setNumber)
                .map((set) => (
                  <ExerciseForm
                    setWorkout={setWorkout}
                    date={workout.dateOfWorkout}
                    ex={ex}
                    set={set}
                  />
                ))}
              <Button onClick={() => addSet(ex.exerciseId)} className="mt-2">
                New Set
              </Button>
            </div>
          ))
        ) : (
          <Empty>
            <EmptyHeader>
              <EmptyMedia variant="icon">
                <Ghost />
              </EmptyMedia>
              <EmptyTitle>Nothing To See Here</EmptyTitle>
              <EmptyDescription>
                Add your first exercise for today by using the button below!
              </EmptyDescription>
            </EmptyHeader>
          </Empty>
        )}

        <NewWorkout setWorkout={setWorkout} />
      </div>
    </div>
  );
};
export default WorkoutForm;
