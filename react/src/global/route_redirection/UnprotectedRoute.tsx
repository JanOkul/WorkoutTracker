import { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { verifyAuth, type protectedRouteProp } from "./RouteRedirectionUtils";

/**
 * Redirects or allows users from a page that they should not be allowed to access given their authentication state.
 * @param redirectPath Path to redirect user to if the user is not allowed in the path.
 * @param isAuthenticated If the user should or should not be authenticated inside the protected route.
 * @param message A message to be shown to the user when they're being redirected
 * @returns Either a navigation component if not allowed, or the children if allowed.
 */
const UnprotectedRoute = ({
  redirectPath,
  message,
  children,
}: protectedRouteProp) => {
  const isAuthenticated = false;
  const [allowed, setAllowed] = useState<boolean | null>(null);

  useEffect(() => {
    async function run() {
      // setAllowed(null);
      const verified = await verifyAuth(isAuthenticated, message);
      setAllowed(verified);
      console.warn(verified);
    }

    run();
  }, [isAuthenticated, message]);

  if (allowed === null) {
    return <div>Loading...</div>;
  }

  if (allowed === false) {
    return <Navigate to={redirectPath} replace />;
  }

  return <>{children}</>;
};

export default UnprotectedRoute;
