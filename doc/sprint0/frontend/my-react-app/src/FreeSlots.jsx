import { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const API_BASE_URL = "http://localhost:8080"; //  Backend is on 8080, NOT 5173

const FreeSlots = () => {
  const [freeSlots, setFreeSlots] = useState([]); //  Initialize as an empty array
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const navigate = useNavigate();
  useEffect(() => {
    // Handle Calendly OAuth redirect without touching Google logic
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get("code");
    const state = urlParams.get("state");
  
    // If Calendly redirected here, exchange the code for a token
    if (code && state === "calendly") {
      const exchangeCalendlyCode = async () => {
        try {
          const response = await axios.post("http://localhost:8080/api/calendly/exchange-token", {
            code: code,
            redirect_uri: "http://localhost:5173/freeslots",
          });
  
          if (response.data && response.data.access_token) {
            localStorage.setItem("calendly_access_token", response.data.access_token);
            console.log("✅ Calendly access token saved.");
            window.history.replaceState({}, document.title, "/freeslots");
          }
        } catch (err) {
          console.error("Calendly token exchange error:", err);
          setError("Failed to authenticate with Calendly.");
        }
      };
  
      exchangeCalendlyCode();
      return; // Don't fetch anything else until redirect is cleaned up
    }

  
    const fetchFreeSlots = async () => {
      try {
        const accessToken = localStorage.getItem("google_access_token");

        if (!accessToken) {
          setError("User is not authenticated. Redirecting to login...");
          setTimeout(() => navigate("/"), 2000);
          return;
        }

        console.log(" Fetching free slots from backend...");

        const response = await axios.get(`${API_BASE_URL}/api/google/calendar/free-slots`, {
          headers: { Authorization: `Bearer ${accessToken}` },
        });

        console.log("API Response:", response.data);

        if (Array.isArray(response.data)) {
          setFreeSlots(response.data);
        } else {
          console.error(" Unexpected API response format:", response.data);
          setError("Invalid data received from server.");
        }
      } catch (err) {
        console.error("API Error:", err);

        //  Handle unauthorized errors properly
        if (err.response && err.response.status === 401) {
          setError("Session expired. Redirecting to login...");
          localStorage.removeItem("google_access_token");
          setTimeout(() => navigate("/"), 2000);
        } else {
          setError("Failed to fetch free slots. Please try again.");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchFreeSlots();
  }, [navigate]);

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-100">
      {/*  Back button */}
      <button
        onClick={() => {
          sessionStorage.setItem("justLoggedIn", "false");
          navigate("/");
        }}
        className="self-start ml-6 mb-4 px-4 py-2 bg-gray-300 text-gray-800 rounded hover:bg-gray-400 transition"
      >
        ← Back to Home
      </button>

      <h1 className="text-3xl font-bold mb-6">Your Available Time Slots</h1>

      {loading ? (
        <p className="text-gray-600">🔄 Loading...</p>
      ) : error ? (
        <p className="text-red-500">{error}</p>
      ) : freeSlots.length === 0 ? (
        <p className="text-gray-600">No free slots available this month.</p>
      ) : (
        <ul className="bg-white p-6 rounded-lg shadow-md w-2/3 max-w-lg">
          {freeSlots.map((slot, index) => (
            <li key={index} className="border-b py-2">🕒 {slot}</li>
          ))}
        </ul>
      )}
    </div>
  );
};



export default FreeSlots;
