import { useState } from 'react'
import axios from "axios";
import './App.css'

import CalendlyAuth from './CalendlyAuth';

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
      <h1>Vite + React</h1>
      <div className="card">
        <button onClick={testBackend}> Press me!</button>
        <div>
          {responseData}
        </div>
        <CalendlyAuth />
      </div>
    </>
  )
}

export default App
