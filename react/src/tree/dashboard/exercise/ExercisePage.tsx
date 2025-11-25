import apiAxios from "@/api/axiosInterceptor";
import { useExerciseList } from "@/global/ExerciseListContext";
import { capitalizeWords } from "@/global/utils";
import { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import type { SingleSet } from "../log/LogUtils";

import { Button } from "@/components/ui/button";
import { ButtonGroup } from "@/components/ui/button-group";
import {
  HoverCard,
  HoverCardContent,
  HoverCardTrigger,
} from "@/components/ui/hover-card";
import { Separator } from "@/components/ui/separator";
import { ArrowLeft, Info } from "lucide-react";
import {
  Legend,
  Line,
  LineChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";
import { epley1RM } from "./stats";

interface workoutEntryISOString {
  dateOfWorkout: string;
  sets: SingleSet[];
}

interface workoutEntryDate {
  date: Date;
  sets: SingleSet[];
}

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
    if (exerciseParam) {
      getExerciseData();
    }
  }, [exerciseParam]);

  if (!exerciseParam) {
    return;
  }

  const exerciseName = exerciseParam.replace("-", " ");
  const exercise = items.find((exercise) => exercise.id === exerciseName);

  if (!exercise) {
    return;
  }

  async function getExerciseData() {
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

      setExerciseRawData(data);
    } catch (err) {
      setExerciseRawData([]);
    }
  }

  const exerciseAggrData = (exerciseRawData ?? [])
    .map((entry) => ({
      date: entry.date.getTime(),
      avg:
        entry.sets.reduce(
          (acc, val) => acc + epley1RM(val.weight, val.reps),
          0
        ) / entry.sets.length,
      max: entry.sets.reduce(
        (acc, val) => Math.max(acc, epley1RM(val.weight, val.reps)),
        -Infinity
      ),
    }))
    .filter((entry) => {
      const start = new Date().getTime() - startDate * 24 * 60 * 60 * 1000;
      return entry.date >= start;
    });

  console.log(exerciseAggrData);

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
            <ButtonGroup>
              <Button
                variant={startDate === 7 ? "default" : "ghost"}
                onClick={() => setStartDate(7)}
              >
                {" "}
                7 Days
              </Button>
              <Button
                variant={startDate === 30 ? "default" : "ghost"}
                onClick={() => setStartDate(30)}
              >
                {" "}
                30 Days
              </Button>
              <Button
                variant={startDate === 90 ? "default" : "ghost"}
                onClick={() => setStartDate(90)}
              >
                {" "}
                90 Days
              </Button>
            </ButtonGroup>
            <div className="flex ml-auto items-center">
              <HoverCard openDelay={150} closeDelay={0}>
                <HoverCardTrigger>
                  <Info size={18} className="hover:bg-gray-300 rounded-full" />
                </HoverCardTrigger>
                <HoverCardContent side="top" align="end" className="w-120">
                  <h4>Calculation</h4>
                  <ul>
                    <li>
                      Each point represents a workout day for this exercise.
                    </li>
                    <li>
                      1RM is estimated using the Epley formula for each set.
                    </li>
                    <Separator className="mt-2 mb-2" />
                    <li>
                      <strong>Average 1RM:</strong> Mean of all sets that day.
                    </li>
                    <li>
                      <strong>Max 1RM:</strong> Best set that day.
                    </li>
                  </ul>
                </HoverCardContent>
              </HoverCard>
            </div>
          </div>
          <ResponsiveContainer width="100%" height={450} className="mt-4">
            <LineChart
              data={exerciseAggrData}
              margin={{ top: 5, right: 5, left: 5, bottom: 5 }}
            >
              <XAxis
                dataKey="date"
                type="number"
                tickFormatter={(value) => {
                  const date = new Date(value);
                  return date.toLocaleDateString("en-US", {
                    month: "short",
                    day: "numeric",
                  });
                }}
                domain={["data-min", "data-max"]}
                padding={{ left: 10, right: 10 }}
                label={{
                  value: "Date Of Workout",
                  offset: 10,
                  position: "bottom",
                }}
              />

              <YAxis
                label={{
                  value: "Weight (kg)",
                  angle: -90,
                  offset: -10,
                  position: "left",
                }}
              />

              <Line dataKey="avg" stroke="#3B82F6" />
              <Line dataKey="max" stroke="#FB923C" />
              <Tooltip
                formatter={(value, name) => [
                  (value as number).toFixed(2),
                  name === "avg" ? "Avg 1 Rep Max" : "Peak 1 Rep Max",
                ]}
                labelFormatter={(date) => new Date(date).toLocaleDateString()}
              />
              <Legend
                align="right"
                formatter={(value) => {
                  if (value === "avg") {
                    return "Average";
                  }

                  if (value === "max") {
                    return "Peak";
                  }
                }}
              />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
};

export default ExercisePage;
