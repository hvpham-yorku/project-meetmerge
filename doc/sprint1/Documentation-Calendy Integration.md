```md
#  MeetMerge: Calendly Link Submission & Validation

This module allows users to submit up to 5 Calendly links through the frontend. Each link is validated for format and existence using the Calendly API.

---

##  Frontend: `CalendlyInputForm.jsx`

### Overview

Allows the user to:
- Input up to **5 Calendly links**
- Add new link fields dynamically
- Validate links against a regex format
- Display error messages for invalid links
- Submit all valid links to the backend

### Imports

```js
import { useState } from 'react';
import axios from "axios";
```

### State

```js
const [links, setLinks] = useState(['']);     // Stores user input
const [errors, setErrors] = useState([]);     // Stores error messages
```

### Validation Logic

```js
const regex = /^(https?:\/\/calendly\.com\/[a-zA-Z0-9_-]+(\/[a-zA-Z0-9_-]+)?)$/;
```

Each link is tested against the regex pattern before submission.

### Event Handlers

#### `handleLinkChange(index, event)`
Updates the value of a specific input field.

#### `handleAddLink()`
Adds a new empty input field (max: 5 links).

#### `handleSubmit()`
Validates links and sends them to the backend if all are valid.

---

## 📡 API: CalendlyController.java

### Endpoint: Validate Calendly Links

```http
POST /calendly/validate-links
```

**Request Body**
```json
[
  "https://calendly.com/user1",
  "https://calendly.com/user2/meeting"
]
```

**Response**
- `200 OK`: All links are valid.
- `400 Bad Request`: Returns error if any link is invalid.

### Environment Setup

- Load API Key securely from `.env` using:
```java
Dotenv dotenv = Dotenv.load();
String API_KEY = dotenv.get("CALENDLY_API_KEY");
```

### Link Format Validation

```java
String regex = "^(https?://calendly\\.com/[a-zA-Z0-9_-]+(/[a-zA-Z0-9_-]+)?)$";
```

### Calendly API Check

Sends a `GET` request to:

```http
https://api.calendly.com/users/me/scheduled_events
```

with `Authorization: Bearer <CALENDLY_API_KEY>` to verify link ownership or existence.

---

##  Error Handling

### Frontend

- Invalid links show a red error message below the corresponding input.
- Max 5 link fields enforced via button disable.

### Backend

- Invalid regex → `400 Bad Request` with error message.
- Link existence check fails → `400 Bad Request` with failure reason.

---

## Deployment Notes

### Environment Variables

In `.env` (backend root):
```env
CALENDLY_API_KEY=your_api_key_here
```

### CORS

Ensure frontend can reach backend (if on different ports):
```java
@CrossOrigin(origins = "http://localhost:5173")
```

---

## Future Enhancements

- Support link previews after validation
- Auto-detect links from clipboard
- Show detailed error for each failed link
- Rate limiting or throttling to avoid excessive API calls

```

Let me know if you want a `.md` file exported or if you'd like this integrated with the existing doc from earlier!
