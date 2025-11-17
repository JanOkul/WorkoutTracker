import apiAxios from "@/api/axiosInterceptor";
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
import { Input } from "@/components/ui/input";
import { capitalizeWords } from "@/global/utils";
import { Check, Trash } from "lucide-react";
import { useEffect, useState, type FormEvent } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  deleteTag,
  formatDate,
  updateTag,
  type Exercise,
  type Set,
  type Workout,
} from "./LogUtils";

const Log = () => {
  const params = useParams();
  const { date } = params;
  const dateApiUrl = `/api/workout/${date}`;
  const navigate = useNavigate();
  const [workout, setWorkout] = useState<Workout>({
    dateOfWorkout: date,
    exercises: [],
  } as Workout);

  async function loadWorkout() {
    try {
      const response = await apiAxios.get(dateApiUrl);
      console.log(response.data);
      setWorkout(response.data);
    } catch (e) {
      console.log("Error fetching workout" + e);
      navigate("/error");
    }
  }

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
      console.log("Created set:", response.status);
    } catch (err) {
      console.error("Failed to add new set:", err);
      deleteSet(exerciseId, newSetNumber); // rollback if needed
    }
  }

  function updateSet(
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

  function deleteSet(exerciseId: string, setNumber: number) {
    setWorkout((prev) => {
      if (!prev) return prev;

      return {
        ...prev,
        exercises: prev.exercises.map((exercise) =>
          exercise.exerciseId === exerciseId
            ? {
                ...exercise,
                sets: exercise.sets.filter(
                  (set) => set.setNumber !== setNumber
                ),
              }
            : exercise
        ),
      };
    });
  }

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

      updateSet(body.exerciseId, body.setNumber, body.weight, body.reps);
    }

    if (method === deleteTag) {
      const body = {
        exerciseId: String(formData.get("exerciseId")),
        setNumber: Number(formData.get("setNumber")),
      };

      await apiAxios.delete(dateApiUrl, {
        data: body,
      });

      deleteSet(body.exerciseId, body.setNumber);
    }
  }

  // Creates a new Exercise entry into the current workout
  function createExercise(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);

    const name = formData.get("name")?.toString().toLowerCase();

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

      const idAlreadyExists = prev.exercises.some(
        (ex) => ex.exerciseId === name
      );

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

  useEffect(() => {
    loadWorkout();
  }, [date]);

  function createWorkout(workout: Workout) {
    return (
      <div className="border-2 rounded-lg p-2 pb-4 pt-4">
        <h2 className="text-center">
          {formatDate(new Date(workout.dateOfWorkout))}
        </h2>

        {/* For each exercise in the workout*/}
        <div className="flex1 justify-center">
          {workout.exercises.length > 0 ? (
            workout.exercises.map((ex) => (
              <div key={ex.exerciseId} className="m-4">
                <h3 className="mb-2">{capitalizeWords(ex.exerciseId)}</h3>

                {/* For each set in an exercise*/}
                <div className="flex space-x-3">
                  <h5 className="w-1/2">Weight</h5>

                  <h5 className="w-1/2">Reps</h5>
                  <Button onClick={() => {}} className="opacity-0 h-7" />
                  <Button onClick={() => {}} className="opacity-0 h-7 mr-0.5" />
                </div>
                {ex.sets
                  .sort((a: Set, b: Set) => a.setNumber - b.setNumber)
                  .map((set) => createExerciseForm(ex, set))}
                <Button onClick={() => addSet(ex.exerciseId)} className="mt-2">
                  New Set
                </Button>
              </div>
            ))
          ) : (
            <div className="w-3/4 mx-auto p-4">
              <h4 className="text-center">
                Ready to start? Add your first exercise below!
              </h4>
            </div>
          )}
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
                    This exercise won't be saved until you add atleast 1 set to
                    the exercise.
                  </DialogDescription>
                </DialogHeader>
                <form onSubmit={createExercise}>
                  <Input name="name" />
                  <DialogFooter className="sm:justify-start">
                    <DialogClose asChild className="mt-4">
                      <Button type="submit">Create</Button>
                    </DialogClose>
                  </DialogFooter>
                </form>
              </DialogContent>
            </Dialog>
          </div>
        </div>
      </div>
    );
  }

  function createExerciseForm(ex: Exercise, set: Set) {
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
  }

  return (
    <div className="max-w-xl mx-auto p-4 sm:p-6">{createWorkout(workout)}</div>
  );
};

export default Log;
