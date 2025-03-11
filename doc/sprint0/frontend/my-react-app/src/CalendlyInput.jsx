import { useState } from 'react';
import axios from "axios";

const CalendlyInputForm = () => {
  const [links, setLinks] = useState(['']);
  const [errors, setErrors] = useState([]);

  const validateLink = (link) => {
    const regex = /^(https?:\/\/calendly\.com\/[a-zA-Z0-9_-]+(\/[a-zA-Z0-9_-]+)?)$/;
    return regex.test(link);
  };

  const handleLinkChange = (index, event) => {
    const newLinks = [...links];
    newLinks[index] = event.target.value;
    setLinks(newLinks);
  };

  const handleAddLink = () => {
    if (links.length < 5) {
      setLinks([...links, '']);
    }
  };

  const handleSubmit = () => {
    const newErrors = links.map(link => (validateLink(link) ? null : "Invalid Calendly URL"));
    setErrors(newErrors);

    if (newErrors.every(err => err === null)) {
      // Submit the links to backend
    
    }
  };

  return (
    <div>
      <h2>Paste Calendly Links</h2>
      {links.map((link, index) => (
        <div key={index}>
          <input
            type="text"
            value={link}
            onChange={(e) => handleLinkChange(index, e)}
            placeholder={`Calendly Link ${index + 1}`}
          />
          {errors[index] && <span style={{ color: 'red' }}>{errors[index]}</span>}
        </div>
      ))}
      <button onClick={handleAddLink} disabled={links.length >= 5}>Add Another Calendly Link</button>
      <button onClick={handleSubmit}>Submit</button>
    </div>
  );
};

export default CalendlyInputForm;
