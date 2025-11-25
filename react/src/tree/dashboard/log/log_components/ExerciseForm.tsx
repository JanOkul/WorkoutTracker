import apiAxios from "@/api/axiosInterceptor";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Check, Trash } from "lucide-react";
import type { FormEvent } from "react";
import { logUrl } from "../Log";
import {
  deleteSet,
  deleteTag,
  updateSet,
  updateTag,
  type Exercise,
  type SingleSet,
  type Workout,
} from "../LogUtils";

interface exerciseFormProp {
  setWorkout: React.Dispatch<React.SetStateAction<Workout>>;
  date: string;
  ex: Exercise;
  set: SingleSet;
}

const ExerciseForm = ({ setWorkout, date, ex, set }: exerciseFormProp) => {
  const dateApiUrl = `${logUrl}${date}`;

  async function exerciseSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const submitter = (e.nativeEvent as SubmitEvent)
      .submitter as HTMLButtonElement;
    const method = submitter?.value;

    const formData = new FormData(e.currentTarget);

    if (method === updateTag) {
      const body = {
        exerciseId: String(formData.get("exerciseId")),
        setNumber: Number(formData.get("setNumber")),
        weight: Number(formData.get("weight")),
        reps: Number(formData.get("reps")),
      };

      await apiAxios.put(dateApiUrl, body);

      updateSet(
        setWorkout,
        body.exerciseId,
        body.setNumber,
        body.weight,
        body.reps
      );
    }

    if (method === deleteTag) {
      const body = {
        exerciseId: String(formData.get("exerciseId")),
        setNumber: Number(formData.get("setNumber")),
      };

      await apiAxios.delete(dateApiUrl, {
        data: body,
      });

      deleteSet(setWorkout, body.exerciseId, body.setNumber);
    }
  }

  return (
    <form
      key={set.setNumber}
      onSubmit={exerciseSubmit}
      className="flex space-x-2 space-y-2 "
    >
      <Input
        name="weight"
        type="number"
        min="0"
        defaultValue={set.weight}
        className="w-1/2"
      />

      <Input
        name="reps"
        type="number"
        min="0"
        defaultValue={set.reps}
        className="w-1/2"
      />

      <Input type="hidden" name="date" value={date} />
      <Input type="hidden" name="exerciseId" value={ex.exerciseId} />
      <Input type="hidden" name="setNumber" value={set.setNumber} />

      <Button name="_method" type="submit" value={updateTag} size="icon">
        <Check />
      </Button>
      <Button name="_method" type="submit" value={deleteTag} size="icon">
        <Trash />
      </Button>
    </form>
  );
};

export default ExerciseForm;
