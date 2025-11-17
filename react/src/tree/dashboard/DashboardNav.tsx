import { Calendar } from "@/components/ui/calendar";

import {
  NavigationMenu,
  NavigationMenuContent,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
  NavigationMenuTrigger,
  navigationMenuTriggerStyle,
} from "@/components/ui/navigation-menu";
import { Link, useNavigate } from "react-router-dom";

const DashboardNav = () => {
  const navigate = useNavigate();

  return (
    <div>
      <NavigationMenu>
        <NavigationMenuList>
          <NavigationMenuItem>
            <NavigationMenuLink
              asChild
              className={navigationMenuTriggerStyle()}
            >
              <Link to="/dashboard"> Home </Link>
            </NavigationMenuLink>
          </NavigationMenuItem>

          <NavigationMenuItem>
            <NavigationMenuTrigger>Log</NavigationMenuTrigger>
            <NavigationMenuContent>
              <Calendar
                mode="single"
                onSelect={(date) => {
                  if (!date) return;
                  const month = date.getMonth() + 1;
                  const day = date.getDate();

                  const formattedMonth = String(month).padStart(2, "0");
                  const formattedDay = String(day).padStart(2, "0");
                  const url = `/dashboard/log/${date?.getFullYear()}-${formattedMonth}-${formattedDay}`;

                  navigate(url);
                }}
                disabled={{ after: new Date() }}
                className="rounded-lg border "
              />
            </NavigationMenuContent>
          </NavigationMenuItem>
        </NavigationMenuList>
      </NavigationMenu>
    </div>
    // <div>
    //     <nav>
    //         <Link to="/dashboard">Dashboard</Link>
    //         <Link to={todayLog}>Log</Link>
    //         <Link to="">Exercises</Link>
    //     </nav>
    // </div>
  );
};

export default DashboardNav;
