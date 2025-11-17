import apiAxios from "@/api/axiosInterceptor";
import { Button } from "@/components/ui/button";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Tile from "../../global/Tile";
import { getTodayDate } from "./Dashboard";

const DashboardMain = () => {
  const [greeting, setGreeting] = useState("");
  const workoutTodayLink = `/dashboard/log/${getTodayDate()}`;

  /**
   * Generate a greeting message to the user.
   * @returns A promise which generates the greeting message.
   */
  const composeGreeting = async (): Promise<string> => {
    return getCurrentTimeGreeting() + ", " + (await getDisplayName());
  };

  /**
   * Converts the current time into which part of day it is (Morning, Afternoon, Evening, Night)
   * @returns Which part of day it is
   */
  function getCurrentTimeGreeting(): string {
    const date = new Date();
    const minutesIntoDay = date.getHours() * 60 + date.getMinutes();

    const morningStart = 0;
    const afternoonStart = 12 * 60;
    const eveningStart = 17 * 60;
    const nightStart = 21 * 60;
    const nightEnd = 24 * 60;

    if (minutesIntoDay >= morningStart && minutesIntoDay < afternoonStart) {
      return "Good Morning";
    }

    if (minutesIntoDay >= afternoonStart && minutesIntoDay < eveningStart) {
      return "Good Afternoon";
    }

    if (minutesIntoDay >= eveningStart && minutesIntoDay < nightStart) {
      return "Good Evening";
    }

    if (minutesIntoDay >= nightStart && minutesIntoDay < nightEnd) {
      return "Good Evening";
    }

    return "Hello";
  }

  /**
   *  Fetches the user's display name from the backend.
   * @returns A promise which fetches the display name.
   */
  const getDisplayName = async (): Promise<string> => {
    const apiUrl = "api/misc/get-display-name";
    let displayName = "Guest";

    try {
      const response = await apiAxios.get(apiUrl);
      displayName = response.data.displayName;
      console.log(displayName);
    } catch (err) {
      console.error(err);
    }

    return displayName;
  };

  useEffect(() => {
    const update = async () => {
      const greeting = await composeGreeting();
      setGreeting(greeting);
    };

    update();
  }, []);

  return (
    <div className="m-4 flex flex-col flex-1 min-h-20">
      <div className="mt-4 mb-4">
        <h2 className="text-center m-4">{greeting}</h2>
      </div>

      <div className="flex flex-col md:flex-row gap-4 flex-1">
        <Tile>
          <h3 className="text-center"> Today's Workout</h3>
          <Button asChild className="m-4">
            <Link to={workoutTodayLink}>Add</Link>
          </Button>
        </Tile>
        <Tile>
          <h3> Today's Workout</h3>
          <Link to={workoutTodayLink}>Add</Link>
        </Tile>
      </div>
    </div>
  );
};

export default DashboardMain;
