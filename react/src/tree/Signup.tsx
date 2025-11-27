import apiAxios from "@/api/axiosInterceptor";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardAction,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import axios from "axios";
import { type FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";

const SignUp = () => {
  const navigate = useNavigate();

  async function signupAction(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const signupData = new FormData(e.currentTarget);
    const apiUrl = "/api/auth/signup";

    const requestBody = {
      email: signupData.get("email"),
      password: signupData.get("password"),
      confirmPassword: signupData.get("confirmPassword"),
      age: signupData.get("age"),
      weight: signupData.get("weight"),
    };

    try {
      await apiAxios.post(apiUrl, requestBody);
      navigate("/dashboard");
    } catch (err) {
      if (axios.isAxiosError(err) && err.response) {
        console.error("Signup failed:", err.response.status, err.response.data);
      } else {
        console.error("Action Failed (Network Error):", err);
      }
      navigate("/signup");
    }
  }

  return (
    <div className="flex flex-col justify-center items-center h-full p-1">
      <Card className="w-full max-w-sm">
        <CardHeader>
          <CardTitle>Sign Up</CardTitle>
          <CardAction>
            <Button variant="link" asChild>
              <Link to="/login">Log In</Link>
            </Button>
          </CardAction>
        </CardHeader>
        <CardContent>
          <form onSubmit={signupAction}>
            <Label htmlFor="email" className="pb-2">
              Email Address
            </Label>
            <Input id="email" name="email" type="email" required />

            <Label htmlFor="password" className="pb-2 pt-4">
              Password
            </Label>
            <Input id="password" name="password" type="password" required />

            <Label htmlFor="confirmPassword" className="pb-2 pt-4">
              Confirm Password
            </Label>
            <Input
              id="confirmPassword"
              name="confirmPassword"
              type="password"
              required
            />

            <Label htmlFor="age" className="pb-2 pt-4">
              Age
            </Label>
            <Input
              id="age"
              name="age"
              type="number"
              min="16"
              max="200"
              required
            />

            <Label htmlFor="weight" className="pb-2 pt-4">
              Weight (kg)
            </Label>
            <Input id="weight" name="weight" type="number" min="1" required />

            <Button type="submit" className="mt-6 w-full">
              Sign Up
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};

export default SignUp;
