# Release Planning Meeting — MeetMerge

**File**: `RPM.md`  
**Date**: 2025-03-20  
**Release Version**: v0.1  

## Participants  
- Rebec (Frontend & OAuth integration)  
- [Teammate 2] (Backend & API integration)  
- [Teammate 3] (UI/UX & Google Calendar logic)  
- [Teammate 4] (Testing & Deployment)  

---

## Release Goal  
Deliver a working MVP of **MeetMerge**, an app that aggregates availability from Calendly, Outlink, and Google Calendar links, finds overlapping time slots, and allows users to generate a new scheduling link or Lettucemeet poll based on those overlaps.

---

## Scope of the Project  

### Epics / Key Features
- Multi-link input & validation (Calendly & Google Calendar)
- Google OAuth integration
- Fetch & normalize availability across time zones
- Identify overlapping time slots
- Display mutual availability in a clean UI
- Generate a new Calendly event or Lettucemeet poll with selected times

---

##  Core User Stories

| # | Title | Description | Status |
|---|-------|-------------|--------|
| 1 | **Link Input** | Allow users to input multiple Calendly and Google links | ✅ In Progress |
| 2 | **Google, Canlendy and Outlook Calendar Support** | Fetch availability via shared Calendar links | ✅ In Progress |
| 3 | **Find Overlapping Times** | Show mutual availability across calendars | 🔜 Planned |
| 4 | **Display Results Clearly** | Local timezone-based results view | 🔜 Planned |
| 5 | **Create New Calendly Link** | Generate link based on overlap | ⏳ Backlog |
| 6 | **Lettucemeet Integration** | Optional link generation with Lettucemeet | ⏳ Backlog |
| 7 | **User Account Creation** | Register/login users for saving data | ⏳ Backlog |
| 8 | **User Login** | Authenticate returning users | ⏳ Backlog |
| 9 | **Save Calendar Combinations** | Allow reuse of saved sets | ⏳ Backlog |

---

## Technical Priorities

- ✅ Finalize Google, Calendy and Outlook OAuth + token handling  
- ✅ Secure frontend-backend callback flow (`/auth/callback`)  
- ✅ Implement `/frontslots` endpoint for fetching calendar data  
- 🔄 Implement overlap comparison logic (time-normalized)  
- 🧪 Display results on `/freeslots` page  
- ⏳ Integrate Calendly/Lettucemeet generation

---

## Timeline

| Week | Tasks |
|------|-------|
| Week 1 | Google, Outlook, Calendy OAuth, Link UI & validation |
| Week 2 | Calendar data fetch & backend endpoint |
| Week 3 | Overlap logic + display results |
| Week 4 | Generate link + final testing and MVP demo |

---

## Additional Notes
- Initial authentication issues resolved by aligning frontend redirect URI and backend callback URI.
- API call errors fixed by updating the `/calendar/free-slots` route to `/frontslots`.
- `google-api-client` library version conflict resolved by adjusting dependencies.

