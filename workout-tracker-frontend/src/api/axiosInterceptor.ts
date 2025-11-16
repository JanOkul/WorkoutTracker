import type { isAuthResponse } from "@/tree/Login";
import axios from "axios";

const apiAxios = axios.create({
  baseURL: "/",
  withCredentials: true,
});

const authAxios = axios.create({
  baseURL: "/",
  withCredentials: true,
});

//infinite loop
apiAxios.interceptors.response.use(
  (response) => {
    // If response is successful, just return it
    console.log(
      "Successfull response from ",
      response.config.baseURL,
      response.status
    );
    return response;
  },
  async (error) => {
    const originalRequest = error.config;

    const excludedUrls = [
      "api/auth/status",
      "api/auth/refresh",
      "api/auth/login",
    ];

    // Check if the request URL should be excluded
    const isExcluded = excludedUrls.some((url) =>
      originalRequest.url?.includes(url)
    );

    // Skip interceptor logic for excluded URLs
    if (isExcluded) {
      return Promise.reject(error);
    }

    // Check if error is 403 and we haven't already retried
    if (error.response?.status === 403 && !originalRequest._retry) {
      originalRequest._retry = true; // Prevent infinite loop
      console.log(
        "Successfull response from ",
        error.config.baseURL,
        error.status
      );
      try {
        // Step 1: Check authentication status
        const statusResponse = await authAxios.get<isAuthResponse>(
          "api/auth/status"
        );

        // Step 2: If authenticated, refresh the token
        if (statusResponse.data.isAuthenticated === true) {
          await authAxios.post("api/auth/refresh", {});

          // Step 3: Retry the original request
          return authAxios(originalRequest);
        } else {
          // Not authenticated, redirect to login
          window.location.href = "/login";
          return Promise.reject(error);
        }
      } catch (refreshError) {
        // If refresh fails, redirect to login
        console.error("Token refresh failed:", refreshError);
        window.location.href = "/login";
        return Promise.reject(refreshError);
      }
    }

    // For other errors, just reject
    return Promise.reject(error);
  }
);

export default apiAxios;
