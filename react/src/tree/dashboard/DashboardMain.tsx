import apiAxios from "@/api/axiosInterceptor";
import { useEffect, useState } from "react";
import { getTodayDate } from "./Dashboard";
import Log from "./log/Log";

const DashboardMain = () => {
  const [greeting, setGreeting] = useState("");

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
    } catch (err) {
      console.log(err);
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
    <div className="m-4 flex flex-col items-center  min-h-20">
      <div className="mt-4 mb-4">
        <h2 className="text-center m-4">{greeting}</h2>
      </div>

      <div className="flex md:flex-row flex-col gap-4">
        <Log dateProp={getTodayDate()} />
      </div>
    </div>
  );
};

export default DashboardMain;
