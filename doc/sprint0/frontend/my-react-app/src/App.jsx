import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom"; 
import axios from "axios";
import "./App.css";
const REDIRECT_URI = "http://localhost:5173/api/auth/callback"; 
const API_BASE_URL = "http://localhost:8080"; //  FIXED Backend should be 8080

import CalendlyAuth from './CalendlyAuth';

function App() {
  const [accessToken, setAccessToken] = useState(localStorage.getItem("google_access_token") || "");
  const navigate = useNavigate(); //  Initialize useNavigate

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get("code");

    if (code) {
      console.log(" Processing OAuth Callback...");
      exchangeCodeForToken(code);

    //  Only auto-redirect if user is on `/` and didn't manually go there
    } else if (accessToken && window.location.pathname === "/" && sessionStorage.getItem("justLoggedIn") !== "false") {
      console.log("User already authenticated, redirecting...");
      navigate("/freeslots"); // Redirect to FreeSlots if already logged in
    }
  }, [accessToken, navigate]); //  Ensure this runs when accessToken changes

  //  FIXED: `exchangeCodeForToken`
  const exchangeCodeForToken = async (code) => {
    try {
      // window.history.replaceState({}, document.title, "/"); 

      console.log(" Exchanging code for token...");
      const response = await axios.get(`http://localhost:8080/api/google/auth/callback?code=${code}`);

      console.log(" Response from backend:", response.data);

      if (response.data && response.data.access_token) {
        console.log(" Google Access Token Received:", response.data.access_token);

        // Flag that the user just logged in — prevents home redirection loop
        sessionStorage.setItem("justLoggedIn", "true");

        localStorage.setItem("google_access_token", response.data.access_token);
        setAccessToken(response.data.access_token);
        
        // Redirect to FreeSlots page after successful login
        navigate("/freeslots");
      } else {
        console.error(" No access token received.");
      }
    } catch (error) {
      console.error(" Token exchange error:", error);
    }
  };

  //  FIXED `handleGoogleLogin` now correctly redirects
  const handleGoogleLogin = () => {
    window.location.href = `${API_BASE_URL}/api/google/auth`; //  Redirect to backend OAuth login
  };

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-100">
      <h1 className="text-3xl font-bold mb-6">MeetMerge - Schedule Your Meetings</h1>

      <div className="card bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-lg font-semibold mb-4">Connect Google Calendar</h2>
        {accessToken ? (
  sessionStorage.getItem("justLoggedIn") === "false" ? (
    <button
      onClick={() => {
        navigate("/freeslots");
      }}
      className="px-6 py-3 bg-green-600 text-white font-semibold rounded-lg shadow-md hover:bg-green-700 transition"
    >
      View My Free Slots
    </button>
  ) : (
    <p className="text-green-600">✅ Google Connected. Redirecting...</p>
  )
) : (
  <button
    onClick={handleGoogleLogin}
    className="px-6 py-3 bg-blue-500 text-white font-semibold rounded-lg shadow-md hover:bg-blue-600 transition"
  >
    Connect Google
  </button>
)}

      </div>
      <CalendlyAuth />
    </div>
  );
}

export default App;
