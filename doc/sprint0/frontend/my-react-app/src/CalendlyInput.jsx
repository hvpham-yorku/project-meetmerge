import { useState } from "react";
import axios from "axios";

const CalendlyInputForm = () => {
  const [links, setLinks] = useState([""]);
  const [errors, setErrors] = useState([]); // Add missing errors state
  const [responseMessage, setResponseMessage] = useState("");

  const handleLinkChange = (index, event) => {
    const newLinks = [...links];
    newLinks[index] = event.target.value;
    setLinks(newLinks);
  };

  const handleAddLink = () => {
    if (links.length < 5) {
      setLinks([...links, ""]);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault(); // Prevent page refresh

    try {
      const response = await axios.post("http://localhost:8080/api/calendly/validate", { links }); // Send all links
      setResponseMessage(response.data);
      setErrors([]); // Clear errors on successful submission
    } catch (error) {
      console.error("Error:", error);
      setResponseMessage("Invalid Calendly link or server error.");
    }
  };

  return (
    <div>
      <h2>Paste Calendly Links</h2>
      <form onSubmit={handleSubmit}>
        {links.map((link, index) => (
          <div key={index}>
            <input
              type="text"
              value={link}
              onChange={(e) => handleLinkChange(index, e)}
              placeholder={`Calendly Link ${index + 1}`}
            />
            {errors[index] && <span style={{ color: "red" }}>{errors[index]}</span>}
          </div>
        ))}

        <button type="button" onClick={handleAddLink} disabled={links.length >= 5}>
          Add Another Calendly Link
        </button>
        <button type="submit">Submit</button>
      </form>
      <p>Response: {responseMessage}</p>
    </div>
  );
};

export default CalendlyInputForm;
