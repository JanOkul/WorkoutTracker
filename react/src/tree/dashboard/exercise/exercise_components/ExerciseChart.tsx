import {
  CartesianGrid,
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
    <ResponsiveContainer
      width="100%"
      minHeight={200}
      height="100%"
      className="mt-4"
    >
      <LineChart
        data={data}
        margin={{ top: 5, right: 0, left: -10, bottom: 5 }}
      >
        <CartesianGrid />
        <XAxis
          dataKey="date"
          type="number"
          padding={{ left: 0, right: 5 }}
          tickFormatter={(value) => {
            const date = new Date(value);
            return date.toLocaleDateString("en-US", {
              month: "short",
              day: "numeric",
            });
          }}
          domain={["date-min", "date-max"]}
        />

        <YAxis unit="kg" />

        <Line type="monotone" dataKey="avg" stroke="#3B82F6" />
        <Line type="monotone" dataKey="max" stroke="#FB923C" />
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
