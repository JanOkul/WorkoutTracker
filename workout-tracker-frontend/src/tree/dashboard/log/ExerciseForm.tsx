import { getTodayDate } from "../Dashboard";

const ExerciseForm = ({ workout: workout, date }) => {
  const today = getTodayDate();
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

      <div>
        <h2>Workout: {workout.dateOfWorkout}</h2>

        {/* For each exercise in the workout*/}
        {workout.exercises.map((ex) => (
          <div key={ex.exerciseId}>
            <h3>Exercise: {ex.exerciseId}</h3>

            {/* For each set in an exercise*/}
            {ex.sets.map((set) => (
              <form key={set.setNumber} onSubmit={exerciseSubmit}>
                <label>Weight:</label>
                <input
                  name="weight"
                  type="number"
                  min="0"
                  defaultValue={set.weight}
                />

                <label>Reps:</label>
                <input
                  name="reps"
                  type="number"
                  min="0"
                  defaultValue={set.reps}
                />

                <input type="hidden" name="date" defaultValue={date} />
                <input
                  type="hidden"
                  name="exerciseId"
                  defaultValue={ex.exerciseId}
                />
                <input
                  type="hidden"
                  name="setNumber"
                  defaultValue={set.setNumber}
                />

                <button name="_method" type="submit" value={updateTag}>
                  Update
                </button>
                <button name="_method" type="submit" value={deleteTag}>
                  Delete
                </button>
              </form>
            ))}
            <button>Add New Set</button>
          </div>
        ))}
        <button>Add New Exercise</button>
      </div>
    </>
  );
};

export default ExerciseForm;
