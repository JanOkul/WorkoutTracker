import {
  Legend,
  Line,
  LineChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";

interface aggrData {
  date: number;
  avg: number;
  max: number;
}

interface dataProp {
  data: aggrData[];
}

const ExerciseChart = ({ data }: dataProp) => {
  return (
    <ResponsiveContainer width="100%" height={450} className="mt-4">
      <LineChart data={data} margin={{ top: 5, right: 5, left: 5, bottom: 5 }}>
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
  );
};

export default ExerciseChart;
