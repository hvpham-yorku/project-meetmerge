import { useState, useEffect } from "react";
import axios from "axios";
import "./FreeSlots.css";
import { useNavigate } from "react-router-dom";

const API_BASE_URL = "http://localhost:8080"; //  Backend is on 8080, NOT 5173

const FreeSlots = () => {
  const [freeSlots, setFreeSlots] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    // Handle Calendly OAuth redirect without touching Google logic
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get("code");
    const state = urlParams.get("state");

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
      return;
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
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 px-4">
      {/*  Back button */}
      <button
        onClick={() => {
          sessionStorage.setItem("justLoggedIn", "false");
          navigate("/");
        }}
        className="back-button"
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
        <div className="slot-list">
          {freeSlots.map((slot, index) => {
            const [startRaw, endRaw] = slot.split(" to ");
            const start = new Date(startRaw.split("[")[0]);
            const end = new Date(endRaw.split("[")[0]);
            

            const dateString = start.toLocaleDateString("en-CA", {
              weekday: "long",
              year: "numeric",
              month: "long",
              day: "numeric",
            });

            const startTime = start.toLocaleTimeString("en-CA", {
              hour: "2-digit",
              minute: "2-digit",
            });

            const endTime = end.toLocaleTimeString("en-CA", {
              hour: "2-digit",
              minute: "2-digit",
            });

            const timezone = startRaw.split("[")[1]?.replace("]", "") || "N/A";

            return (
              <div key={index} className="slot-card">
                <p className="slot-date">📅 {dateString}</p>
                <p className="slot-time">🕒 {startTime} — {endTime}</p>
                <p className="slot-zone">🌍 {timezone}</p>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default FreeSlots;
