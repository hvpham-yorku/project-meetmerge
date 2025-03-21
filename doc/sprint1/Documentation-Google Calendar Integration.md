

# MeetMerge Documentation

##  Free Slot Logic

Compare the **end of one event** to the **start of the next**.
- If there's a **gap**, it's a **free slot**.

### Edge Cases Handled
- **All-day events**: Falls back to `.getDate()` if `.getDateTime()` is `null`.
- **Timezone-aware**: Uses `ZonedDateTime` with the system default time zone.

---

##  API Documentation

### 1. Redirect to Google Auth

```http
GET /api/google/auth
```

Redirects user to Google OAuth consent screen.

---

### 2. Handle Callback and Return Access Token

```http
GET /api/google/auth/callback?code=XYZ
```

**Response**
```json
{
  "access_token": "ya29.a0Af..."
}
```

---

### 3. Get Free Time Slots

```http
GET /api/google/calendar/free-slots
Authorization: Bearer <access_token>
```

**Response**
```json
[
  "2025-03-21T10:00 to 2025-03-21T12:00",
  "2025-03-22T15:30 to 2025-03-22T17:00"
]
```

---

## 🖥️ Frontend Overview

### Pages

- `App.jsx` — Entry point. Handles redirect and access token logic.
- `FreeSlots.jsx` — Displays user’s available time slots.

### State

- Uses `useState`, `useEffect`, `useNavigate` from `react-router-dom`.
- Stores access token in `localStorage`.

### Styles

- Tailwind CSS for layout and UI components.

### Error Handling

- Invalid token → redirects to login.
- No token → displays message and auto-redirects.

---

## 🚀 Deployment Notes

### Backend

- Run Spring Boot app on port `8080`.
- Place `client_secret.json` in:

```text
src/main/resources/
```

---

### Frontend

- React app runs on port `5173`.
- Set OAuth redirect URI in Google Cloud to:

```bash
http://localhost:5173/api/auth/callback
```

---

### Environment

- Enable CORS in Spring Boot:
```java
@CrossOrigin(origins = "http://localhost:5173")
```

- Use environment variables or `application.properties` for secrets in production.
```
