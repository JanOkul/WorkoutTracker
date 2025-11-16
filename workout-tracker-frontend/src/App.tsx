import { Outlet } from "react-router-dom";
import Footer from "./global/Footer";
import Header from "./global/Header";
import "./index.css";

function App() {
  return (
    <main className="flex flex-col h-screen">
      <Header />
      <div className=" flex-1 flex flex-col overflow-y-auto">
        <Outlet />
      </div>
      <Footer />
    </main>
  );
}

export default App;
