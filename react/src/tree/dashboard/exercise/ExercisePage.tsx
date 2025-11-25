import { useExerciseList } from "@/global/ExerciseListContext";
import { capitalizeWords } from "@/global/utils";
import { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";

import { Button } from "@/components/ui/button";
import { ArrowLeft } from "lucide-react";
import ExerciseChart from "./ExerciseChart";
import {
  aggregateExerciseData,
  getExerciseData,
  type workoutEntryDate,
} from "./ExerciseUtils";
import OneRepMaxInfo from "./OneRepMaxInfo";
import StartDateSelection from "./StartDateSelection";

const ExercisePage = () => {
  const [exerciseRawData, setExerciseRawData] = useState<workoutEntryDate[]>(
    []
  );
  const [startDate, setStartDate] = useState(7);
  const params = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const { items } = useExerciseList();
  const fromLogsPage = location.state?.fromLogs || false; // Checks if previous page was from the logs pages
  const exerciseParam = params.exerciseName;

  useEffect(() => {
    if (!exerciseParam) {
      return;
    }
    getExerciseData(exerciseParam.replace("-", " "), setExerciseRawData);
  }, [exerciseParam]);

  if (!exerciseParam) {
    return;
  }

  const exerciseName = exerciseParam.replace("-", " ");
  const exercise = items.find((exercise) => exercise.id === exerciseName);

  if (!exercise) {
    return;
  }

  const exerciseAggrData = aggregateExerciseData(exerciseRawData, startDate);

  return (
    <div className="w-full max-w-3xl mx-auto h-fit mt-4">
      {fromLogsPage ? (
        <div className="ml-1">
          <Button onClick={() => navigate(-1)} className="mb-4">
            {" "}
            <ArrowLeft /> Back To Log Page{" "}
          </Button>
        </div>
      ) : (
        <></>
      )}

      <div className="border-2 rounded-lg pb-4 pt-4  p-4 sm:p-6 ">
        <div>
          <h2>{capitalizeWords(exercise.id)}</h2>
          <p>{exercise.description}</p>
        </div>

        <div className="mt-4 lg:mt-6  ">
          <h3>One-Rep Max Progress (Last {startDate} Days)</h3>

          <div className="flex m-2 ml-0 items-center">
            <StartDateSelection
              startDate={startDate}
              setStartDate={setStartDate}
            />

            <OneRepMaxInfo />
          </div>

          <ExerciseChart data={exerciseAggrData} />
        </div>
      </div>
    </div>
  );
};

export default ExercisePage;
