import { RouterProvider } from "react-router-dom"
import {router} from "./main"
import Header from "./global/Header"
import Footer from "./global/Footer"
import "./index.css"

function App() {


  return (
      <div>
        <Header/>
        <RouterProvider router={router}/>
        <Footer/>
      </div>
  )
}

export default App
