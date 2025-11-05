import axios from "axios";
import { useEffect } from "react";
import { Form, useNavigate } from "react-router-dom";
import type { isAuthResponse } from "./Login";

const SignUp = () => {
  const navigate = useNavigate();

  async function getStatus() {
    try {
      const response = await axios.get<isAuthResponse>("/api/auth/status");
      if (response.data.isAuthenticated) {
        console.log("Already authenticated");
        navigate("/dashboard");
      }
    } catch {
      console.log("Unable to check if user is already authenticated");
    }
  }

  useEffect(() => {
    getStatus();
  }, []);

  return (
    <Form method="POST">
      <label htmlFor="email">Email Address</label>
      <input id="email" name="email" type="email" required />
      <label htmlFor="password">Password</label>
      <input id="password" name="password" type="password" required />
      <label htmlFor="confirmPassword">Confirm Password</label>
      <input
        id="confirmPassword"
        name="confirmPassword"
        type="password"
        required
      />
      <label htmlFor="age">Age</label>
      <input id="age" name="age" type="number" min="16" max="200" required />
      <label htmlFor="weight">Weight (kg/lbs)</label>

      <input id="weight" name="weight" type="number" min="1" required />
      <input id="submit" value="Sign Up" type="submit" />
    </Form>
  );
};

export default SignUp;
