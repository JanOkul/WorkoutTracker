import apiAxios from "@/api/axiosInterceptor";
import { useExerciseList } from "@/global/ExerciseListContext";
import { type exerciselist } from "@/global/utils";
import { useEffect } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import DashboardNav from "./DashboardNav";

const Dashboard = () => {
  const { setItems } = useExerciseList();
  const navigate = useNavigate();

  async function getExercises() {
    if (sessionStorage.getItem("exercises") !== null) {
      return;
    }

    // Get exercise list
    try {
      const response = await apiAxios.get<exerciselist>(
        "/api/misc/get-exercise-list"
      );
      setItems(response.data.exercises);
    } catch (err) {
      console.log("Unable to fetch exercise list");
      navigate("/error");
    }
  }

  useEffect(() => {
    getExercises();
  }, []);

  return (
    <>
      <div>
        <DashboardNav />
      </div>
      <Outlet />
    </>
  );
};

export const getTodayDate = (): string => {
  return new Date().toISOString().slice(0, 10);
};

export default Dashboard;
