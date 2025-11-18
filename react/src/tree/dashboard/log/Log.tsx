import apiAxios from "@/api/axiosInterceptor";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { type Workout } from "./LogUtils";
import WorkoutForm from "./log_components/WorkoutForm";

export const logUrl = "/api/workout/";

const Log = () => {
  const params = useParams();
  const { date } = params;
  const navigate = useNavigate();

  if (!date) {
    navigate("/error", { state: { message: "No date provided" } });
    return; // To stop error in WorkoutForm
  }

  const dateApiUrl = `${logUrl}${date}`;
  const [workout, setWorkout] = useState<Workout>({
    dateOfWorkout: date,
    exercises: [],
  } as Workout);

  async function loadWorkout() {
    try {
      const response = await apiAxios.get(dateApiUrl);
      setWorkout(response.data);
    } catch (e) {
      console.log("Error fetching workout" + e);
      navigate("/error");
    }
  }

  useEffect(() => {
    loadWorkout();
  }, [date]);

  return (
    <div className="max-w-xl mx-auto p-4 sm:p-6">
      <WorkoutForm setWorkout={setWorkout} date={date} workout={workout} />
    </div>
  );
};

export default Log;
