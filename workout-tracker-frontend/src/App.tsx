import { RouterProvider } from "react-router-dom";
import Footer from "./global/Footer";
import Header from "./global/Header";
import "./index.css";
import { router } from "./main";

function App() {
  return (
    <div className="flex flex-col h-screen">
      <Header />
      <main className="h-dvh">
        <RouterProvider router={router} />
      </main>
      <Footer />
    </div>
  );
}

export default App;
