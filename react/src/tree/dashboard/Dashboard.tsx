import apiAxios from "@/api/axiosInterceptor";
import { type exerciselist } from "@/global/utils";
import { useEffect } from "react";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import type { isAuthResponse } from "../Login";
import DashboardNav from "./DashboardNav";

const Dashboard = () => {
  const navigate = useNavigate();
  const location = useLocation();

  // Sends user to the home page if they're not logged in.
  async function getStatus() {
    try {
      const response = await apiAxios.get<isAuthResponse>("/api/auth/status");
      if (!response.data.isAuthenticated) {
        console.log("Already authenticated");
        navigate("/");
      }
    } catch {
      console.log("Unable to check if user is already authenticated");
    }
  }

  async function getExercises() {
    if (sessionStorage.getItem("exercises") !== null) {
      return;
    }

    // Get exercise list
    try {
      const response = await apiAxios.get<exerciselist>(
        "/api/misc/get-exercise-list"
      );
      sessionStorage.setItem(
        "exercises",
        JSON.stringify(response.data.exercises)
      );
    } catch (err) {
      console.log("Unable to fetch exercise list");
      navigate("/error");
    }
  }

  useEffect(() => {
    getStatus();
  }, [location]);

  useEffect(() => {
    getExercises();
  }, []);

  return (
    <>
      <DashboardNav />
      <Outlet />
    </>
  );
};

export const getTodayDate = (): string => {
  return new Date().toISOString().slice(0, 10);
};

export default Dashboard;
