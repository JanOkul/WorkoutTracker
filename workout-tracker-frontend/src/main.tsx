import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { createBrowserRouter } from "react-router-dom";
import App from "./App.tsx";
import "./index.css";
import Home from "./tree/Home.tsx";
import Login from "./tree/Login.tsx";
import SignUp from "./tree/Signup.tsx";
import Dashboard from "./tree/dashboard/Dashboard.tsx";
import DashboardMain from "./tree/dashboard/DashboardMain.tsx";
import Log from "./tree/dashboard/log/Log.tsx";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <Home />,
  },

  {
    path: "/login",
    element: <Login />,
  },

  {
    path: "/signup",
    element: <SignUp />,
  },

  {
    path: "/dashboard",
    element: <Dashboard />,
    children: [
      {
        index: true,
        element: <DashboardMain />,
      },

      {
        path: "log/:date",
        element: <Log />,
      },
    ],
  },
]);

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <App />
  </StrictMode>
);
