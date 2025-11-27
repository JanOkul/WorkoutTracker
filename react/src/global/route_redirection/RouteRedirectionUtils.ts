import apiAxios from "@/api/axiosInterceptor";
import type { isAuthResponse } from "@/tree/Login";
import type { ReactNode } from "react";
import { toast } from "sonner";

export interface protectedRouteProp {
  redirectPath: string;
  message: string | undefined;
  children: ReactNode;
}

export async function verifyAuth(
  isAuthenticated: boolean,
  message: string | undefined
) {
  try {
    const response = await apiAxios.get<isAuthResponse>("/api/auth/status");

    if (response.data.isAuthenticated === isAuthenticated) {
      return true;
    }

    if (message !== undefined) {
      toast.error(message);
    }

    return false;
  } catch {
    console.log("Unable to check if user is already authenticated");
    return true;
  }
}
