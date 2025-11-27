import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import App from "./App.tsx";
import ErrorPage from "./global/ErrorPage.tsx";
import { ExerciseListProvider } from "./global/ExerciseListContext.tsx";
import ProtectedRoute from "./global/route_redirection/ProtectedRoute.tsx";
import UnprotectedRoute from "./global/route_redirection/UnprotectedRoute.tsx";
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
        element: (
          <UnprotectedRoute
            redirectPath="/dashboard"
            message="Already signed in."
          >
            <Home />
          </UnprotectedRoute>
        ),
      },

      {
        path: "/login",
        element: (
          <UnprotectedRoute
            redirectPath="/dashboard"
            message="Already signed in."
          >
            <Login />
          </UnprotectedRoute>
        ),
      },

      {
        path: "/signup",
        element: (
          <UnprotectedRoute
            redirectPath="/dashboard"
            message="Already signed in."
          >
            <SignUp />
          </UnprotectedRoute>
        ),
      },

      {
        path: "/dashboard",
        element: (
          <ProtectedRoute
            redirectPath="/login"
            message="Please login to access this page."
          >
            {" "}
            <Dashboard />
          </ProtectedRoute>
        ),
        children: [
          {
            index: true,
            element: <DashboardMain />,
          },

          {
            path: "log/:date",
            element: <Log dateProp={undefined} />,
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
