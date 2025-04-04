import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "./index.css";
import App from "./App.jsx";
import FreeSlots from "./FreeSlots.jsx"; 

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <Router>
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/freeslots" element={<FreeSlots />} />
        <Route path="/api/auth/callback" element={<App />} />
      </Routes>
    </Router>
  </StrictMode>
);
