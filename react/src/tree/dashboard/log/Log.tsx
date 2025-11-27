import apiAxios from "@/api/axiosInterceptor";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { type Workout } from "./LogUtils";
import WorkoutForm from "./log_components/WorkoutForm";

export const logUrl = "/api/workout/";

const Log = ({ dateProp }: { dateProp: string | undefined }) => {
  const params = useParams();

  const date = params.date ? params.date : dateProp;
  const navigate = useNavigate();

  useEffect(() => {
    if (!date) {
      return;
    }
    loadWorkout();
  }, [date]);

  if (!date) {
    return;
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

  return (
    <div className="max-w-xl mx-auto p-2">
      <WorkoutForm setWorkout={setWorkout} date={date} workout={workout} />
    </div>
  );
};

export default Log;
