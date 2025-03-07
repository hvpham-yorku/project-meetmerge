import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
//import { testBackend, responseData} from './api'
import axios from "axios";
import './App.css'

function App() {
  const API_URL = "http://localhost:8080/api/meetings";
const [responseData, setResponseData] = useState("");

const testBackend = async() => {
    try{
    const response = await axios.get(`${API_URL}/test`);
    setResponseData(response.data);
    } catch(error){
        console.error("Error fetching data: ", error);
        setResponseData("Error fetching data.");
    }
};

  return (
    <>
      <div>
        <a href="https://vite.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
      <h1>Vite + React</h1>
      <div className="card">
        <button onClick={testBackend}> Press me!</button>
        <div>
          {responseData}
          </div>
      </div>
      <p className="read-the-docs">
        Click on the Vite and React logos to learn more
      </p>
    </>
  )
}

export default App
