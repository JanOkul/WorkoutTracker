
import { Outlet } from "react-router-dom";
import DashboardNav from "./DashboardNav";


const Dashboard = () => {

  return (
    <div>
      <DashboardNav/>
      <main>
        <Outlet/>
      </main>
    </div>
  )
}

export const getTodayDate = (): string => {return new Date().toISOString().slice(0,10)};

export default Dashboard