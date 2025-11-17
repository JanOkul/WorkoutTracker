import { Button } from "@/components/ui/button";
import { Link } from "react-router-dom";
function Home() {
  return (
    <div className="flex flex-col justify-center items-center h-full">
      <div>
        <h2 className="text-2xl font-semibold text-center">
          Put some interesting sounding text here
        </h2>
      </div>

      <div className="flex justify-center items-center">
        <nav className="space-x-4 mt-4">
          <Button asChild variant="ghost">
            <Link to="/login">Log In</Link>
          </Button>

          <Button asChild>
            <Link to="/signup">Get Started</Link>
          </Button>
        </nav>
      </div>
    </div>
  );
}

export default Home;
