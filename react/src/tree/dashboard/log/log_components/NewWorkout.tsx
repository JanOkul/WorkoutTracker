import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { useExerciseList } from "@/global/ExerciseListContext";
import { useState } from "react";
import { createExercise, type Workout } from "../LogUtils";
import ExerciseSelect from "./ExerciseSelect";

interface newWorkoutProp {
  setWorkout: React.Dispatch<React.SetStateAction<Workout>>;
}

const NewWorkout = ({ setWorkout }: newWorkoutProp) => {
  const { items } = useExerciseList();

  const [comboBoxValue, setComboBoxValue] = useState("");

  return (
    <div className="ml-2 mr-2">
      <Dialog>
        <DialogTrigger asChild>
          <Button variant="outline" className="w-full">
            New Exercise
          </Button>
        </DialogTrigger>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Select your Exercise</DialogTitle>
            <DialogDescription>
              This exercise won't be saved until you add atleast 1 set to the
              exercise.
            </DialogDescription>
          </DialogHeader>
          <ExerciseSelect
            exercises={items}
            value={comboBoxValue}
            setValue={setComboBoxValue}
          />
          <DialogFooter className="sm:justify-start">
            <DialogClose asChild className="mt-4">
              <Button onClick={() => createExercise(setWorkout, comboBoxValue)}>
                Create
              </Button>
            </DialogClose>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default NewWorkout;
