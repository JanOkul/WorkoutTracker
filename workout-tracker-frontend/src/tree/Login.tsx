import { Button } from "@/components/ui/button";
import {
  Card,
  CardAction,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Checkbox } from "@/components/ui/checkbox";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import axios from "axios";
import { useEffect, type FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";
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
        return;
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
    <div className="flex flex-col justify-center items-center h-full">
      <Card className=" w-full max-w-sm">
        <CardHeader>
          <CardTitle>Log In</CardTitle>
          <CardAction>
            <Button variant="link" asChild>
              <Link to="/signup"> Sign Up </Link>
            </Button>
          </CardAction>
        </CardHeader>
        <CardContent>
          <form onSubmit={loginAction}>
            <Label htmlFor="email" className="pb-2">
              Email Address
            </Label>
            <Input id="email" name="email" type="email" />
            <Label htmlFor="password" className="pb-2 pt-4">
              Password
            </Label>
            <Input id="password" name="password" type="password" />
            <div className="flex items-center space-x-2 justify-between w-full">
              <Label htmlFor="rememberMe" className="pb-2 pt-4">
                Remember Me
              </Label>
              <Checkbox id="rememberMe" name="rememberMe" className="" />
            </div>

            <Button id="loginSubmit" value="Log" type="submit" className="mt-6">
              Log In
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};

export default Login;
