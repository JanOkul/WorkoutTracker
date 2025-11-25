import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import App from "./App.tsx";
import ErrorPage from "./global/ErrorPage.tsx";
import { ExerciseListProvider } from "./global/ExerciseListContext.tsx";
import "./index.css";
import Home from "./tree/Home.tsx";
import Login from "./tree/Login.tsx";
import SignUp from "./tree/Signup.tsx";
import Dashboard from "./tree/dashboard/Dashboard.tsx";
import DashboardMain from "./tree/dashboard/DashboardMain.tsx";
import ExercisePage from "./tree/dashboard/exercise/ExercisePage.tsx";
import Log from "./tree/dashboard/log/Log.tsx";

export const mainRouter = createBrowserRouter([
  {
    element: <App />,
    errorElement: <ErrorPage />,
    children: [
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

          {
            path: "exercises/:exerciseName",
            element: <ExercisePage />,
          },
        ],
      },
    ],
  },
]);

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <ExerciseListProvider>
      <RouterProvider router={mainRouter} />
    </ExerciseListProvider>
  </StrictMode>
);
