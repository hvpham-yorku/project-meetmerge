# MeetMerge - Definition of Done 

## Core Backend  
- [ ] Accept multiple **Calendly** and **Google Calendar** links.  
- [ ] Fetch availability data from **Calendly API**.  
- [ ] Fetch availability data from **Google Calendar API**.  
- [ ] Merge availability across users to find overlapping time slots.  
- [ ] Support **Google Calendar sharing links** and correctly pull availability data.  
- [ ] Handle **time zone differences** when merging calendar data.  

---

## Core Frontend  
- [ ] Simple **UI for input and results**.  
  - [ ] Users paste **Calendly** and **Google Calendar** links.  
  - [ ] Display common available time slots in an **easy-to-read format**.  
  - [ ] Show results in a **calendar view** with correct time zones.  
- [ ] Auto-create a **Calendly event** with overlapping slots.  
- [ ] Provide an option to **generate a LettuceMeet poll** with overlapping times.  
- [ ] Notify users if **no overlapping times** are found and suggest nearest matches.  

---

## Refinements & Enhancements  
- [ ] Ensure **accurate time conversions** based on user settings.  
- [ ] Implement **date range filtering** for scheduling results.  
- [ ] Allow users to **specify meeting length** for valid time slots.  
- [ ] Basic **user authentication** (Google OAuth or email-based login).  
- [ ] Allow **users to save calendar combinations** for future use.  
- [ ] Implement **dark mode option** for UI.  
- [ ] Mobile-friendly design: Ensure **all core functions work on mobile**.  

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
- [ ] **User Accounts**: Allow users to sign up and log in to save preferences.  
- [ ] **Exporting Options**: Provide download formats (CSV, iCal) for results.  
- [ ] **Direct Calendar Connection**: Allow users to link Google/Microsoft calendars directly.  
- [ ] **Recurring Meeting Support**: Find overlapping availability for **weekly/monthly** meetings.  
- [ ] **Buffer Time Settings**: Let users add a time gap between meetings.  

---
