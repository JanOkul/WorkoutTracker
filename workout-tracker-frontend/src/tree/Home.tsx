import { Link } from "react-router-dom";

function Home() {
  return (
    <>
      <h2>Welcome</h2>
      <nav>
        <Link to="/login">Log In</Link>
        <Link to="/signup">Sign Up</Link>
      </nav>
    </>
  );
}

export default Home;
