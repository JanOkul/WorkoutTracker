import axios from "axios";
import { useEffect, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";

export interface isAuthResponse {
  isAuthenticated: boolean;
}

const Login = () => {
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

  async function loginAction(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const loginData = new FormData(e.currentTarget);
    const apiUrl = "api/auth/login";

    const requestBody = {
      email: loginData.get("email"),
      password: loginData.get("password"),
      rememberMe: !!loginData.get("rememberMe"),
    };

    try {
      const response = await axios.post(apiUrl, requestBody);

      const accessToken = response.data.accessToken;
      console.log("Login successful. Access Token:", accessToken);

      navigate("/dashboard");
    } catch (err) {
      if (axios.isAxiosError(err) && err.response) {
        console.error("Login failed:", err.response.status, err.response.data);
      } else {
        console.error("Action Failed (Network Error):", err);
      }

      navigate("/login");
    }
  }

  return (
    <form onSubmit={loginAction}>
      <label htmlFor="loginEmail">Email Address</label>
      <input id="loginEmail" name="email" type="email" />
      <label htmlFor="loginPassword">Password</label>
      <input id="loginPassword" name="password" type="password" />
      <label htmlFor="loginRememberMe">Remember Me</label>
      <input id="loginRememberMe" name="rememberMe" type="checkbox" />

      <input id="loginSubmit" value="Log" type="submit" />
    </form>
  );
};

export default Login;
