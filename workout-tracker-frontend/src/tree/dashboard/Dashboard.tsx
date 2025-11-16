import apiAxios from "@/api/axiosInterceptor";
import { useEffect } from "react";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import type { isAuthResponse } from "../Login";
import DashboardNav from "./DashboardNav";

const Dashboard = () => {
  const navigate = useNavigate();
  const location = useLocation();
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
  useEffect(() => {
    getStatus();
  }, [location]);
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
