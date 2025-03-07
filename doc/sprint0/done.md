# MeetMerge - Definition of Done 

## Core Backend
- [ ] Accept multiple **Calendly** and **Google Calendar** links.
- [ ] Fetch availability data from **Calendly API**.
- [ ] Fetch availability data from **Google Calendar API**.
- [ ] Merge availability across users to find overlapping time slots.

---

## Core Frontend
- [ ] Simple **UI for input and results**.
  - [ ] Users paste calendar links.
  - [ ] Display common available time slots.
- [ ] Auto-create a **Calendly event** with overlapping slots.

---

## Refinements & Enhancements
- [ ] Ensure **accurate time conversions** based on user settings.
- [ ] Handle cases where **no overlapping times** exist (suggest nearest matches).
- [ ] Basic **user authentication** (Google OAuth or email-based login).

---

## Deployment & Optimization
- [ ] Full **testing and debugging** of backend and frontend.
- [ ] Optimize **API requests** for performance.
- [ ] Deploy the **backend** on **Vercel/Heroku**.
- [ ] Deploy the **frontend** on **Netlify/Vercel**.

---

## Optional Stretch Features *(if time permits)*
- [ ] **Email Notifications**: Notify users when an event is scheduled.
- [ ] **LettuceMeet Autofill**: Autofill suggested time slots in **LettuceMeet**.

---
