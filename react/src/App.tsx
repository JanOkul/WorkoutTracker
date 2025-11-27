import { Outlet } from "react-router-dom";
import { Toaster } from "sonner";
import Header from "./global/Header";
import "./index.css";

function App() {
  return (
    <main className="flex flex-col h-screen">
      <Header />
      <div className=" flex-1 flex flex-col overflow-y-auto">
        <Toaster richColors position="top-center" />
        <Outlet />
      </div>
    </main>
  );
}

export default App;
