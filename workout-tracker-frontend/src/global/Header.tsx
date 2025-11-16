import apiAxios from "@/api/axiosInterceptor";
import { Button } from "@/components/ui/button";
import { Link, useLocation, useNavigate } from "react-router-dom";

function Header() {
  const location = useLocation();
  const navigate = useNavigate();
  const isDashboard = location.pathname.startsWith("/dashboard");

  async function logout() {
    try {
      await apiAxios.post("/api/auth/logout");
      navigate("/");
    } catch (err) {
      console.log("Failed to logout");
    }
  }

  const loginOrLogout = isDashboard ? (
    <Button onClick={() => logout()}>Log Out</Button>
  ) : (
    <Button asChild>
      <Link to="/login">Log In</Link>
    </Button>
  );
  return (
    <header className="flex p-3">
      <Link to="/" className="w-fit">
        <h2 className="m">Workout Tracker</h2>
      </Link>
      <div className="ml-auto">{loginOrLogout}</div>
    </header>
  );
}

export default Header;
