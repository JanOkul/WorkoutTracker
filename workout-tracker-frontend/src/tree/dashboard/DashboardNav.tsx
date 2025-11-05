import { Link } from "react-router-dom"
import { getTodayDate } from "./Dashboard"


const DashboardNav = () => {
  const todayLog = "/dashboard/log/" + getTodayDate(); 
  return (
    <div>
        <nav>
            <Link to="/dashboard">Dashboard</Link>
            <Link to={todayLog}>Log</Link>
            <Link to="">Exercises</Link>
        </nav>
    </div>
  )
}

export default DashboardNav