import axios from "axios";
import { useEffect, useState, type FormEvent } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getTodayDate } from "../Dashboard";

interface Set {
  setNumber: number;
  weight: number;
  reps: number;
}

interface Exercise {
  exerciseId: number;
  sets: Set[];
}

interface Workout {
  dateOfWorkout: string;
  exercises: Exercise[];
}

const updateTag = "UPDATE";
const deleteTag = "DELETE";

const Log = () => {
  const params = useParams();
  const { date } = params;
  const today = getTodayDate();
  const dateApiUrl = `/api/workout/${date}`;
  const navigate = useNavigate();
  const [workout, setWorkout] = useState<Workout>({
    dateOfWorkout: date,
    exercises: [],
  } as Workout);

  async function loadWorkout() {
    try {
      const response = await axios.get(dateApiUrl);
      setWorkout(response.data);
    } catch (e) {
      console.log("Error fetching workout" + e);
      navigate("/error");
    }
  }

  async function addSet(exerciseId: number) {
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
      const response = await axios.post(dateApiUrl, {
        exerciseId,
        setNumber: newSetNumber,
        weight: 0,
        reps: 0,
      });
      console.log("Created set:", response.status);
    } catch (err) {
      console.error("Failed to add new set:", err);
      deleteExercise(exerciseId, newSetNumber); // rollback if needed
    }
  }

  function updateExercise(
    exerciseId: number | null,
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

  function deleteExercise(exerciseId: number, setNumber: number) {
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
        exerciseId: Number(formData.get("exerciseId")),
        setNumber: Number(formData.get("setNumber")),
        weight: Number(formData.get("weight")),
        reps: Number(formData.get("reps")),
      };

      const response = await axios.put(dateApiUrl, body);
      console.log(response.status);
      updateExercise(body.exerciseId, body.setNumber, body.weight, body.reps);
    }

    if (method === deleteTag) {
      const body = {
        exerciseId: Number(formData.get("exerciseId")),
        setNumber: Number(formData.get("setNumber")),
      };
      const response = await axios.delete(dateApiUrl, {
        data: body,
      });

      console.log(response.status);
      deleteExercise(body.exerciseId, body.setNumber);
    }
  }

  useEffect(() => {
    loadWorkout();
  }, [date]);

  function createWorkout(workout: Workout) {
    return (
      <div>
        <h2>Workout: {workout.dateOfWorkout}</h2>

        {/* For each exercise in the workout*/}
        {workout.exercises.map((ex) => (
          <div key={ex.exerciseId}>
            <h3>Exercise: {ex.exerciseId}</h3>

            {/* For each set in an exercise*/}
            {ex.sets.map((set) => createExerciseForm(ex, set))}
            <button onClick={() => addSet(ex.exerciseId)}>Add New Set</button>
          </div>
        ))}
        <button>Add New Exercise</button>
      </div>
    );
  }

  function createExerciseForm(ex: Exercise, set: Set) {
    return (
      <form key={set.setNumber} onSubmit={exerciseSubmit}>
        <label>Weight:</label>
        <input name="weight" type="number" min="0" defaultValue={set.weight} />

        <label>Reps:</label>
        <input name="reps" type="number" min="0" defaultValue={set.reps} />

        <input type="hidden" name="date" value={date} />
        <input type="hidden" name="exerciseId" value={ex.exerciseId} />
        <input type="hidden" name="setNumber" value={set.setNumber} />

        <button name="_method" type="submit" value={updateTag}>
          Update
        </button>
        <button name="_method" type="submit" value={deleteTag}>
          Delete
        </button>
      </form>
    );
  }

  return (
    <>
      <input
        name="date"
        type="date"
        value={date}
        max={today}
        required
        onChange={(e) => navigate(`/dashboard/log/${e.target.value}`)}
      />
      {createWorkout(workout)}
    </>
  );
};

export default Log;
