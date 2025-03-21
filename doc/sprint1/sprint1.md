# Sprint 1 Planning Meeting — MeetMerge  

**File**: `sprint1.md`  
**Dates**: 2025-03-9, 2025-03-16, 2025-03-18, 2025-03-20  
**Sprint Duration**: 2 Weeks  
**Sprint Goal**:  
Deliver core functionality for **MeetMerge**, focusing on Google Calendar, Outlook, Calendly integration, ensuring users can input their scheduling links and fetch availability.  

---

## Participants  
- **Rebecca** (Google Calendar integration, RPM.md, documentation, sprint1.md)  
- **Emmanuel** (Calendly integration, Trello tracking, README, setup)  
- **Amir** (Outlook support, system design)  

---

## Sprint Goal  
The objective of this sprint is to implement Google Calendar and Calendly integration, allowing users to input their scheduling links and retrieve availability for finding common free times. This will serve as the foundation for the meeting coordination functionality.  

---

## Stories Selected for Sprint 1  

| # | User Story | Description | Assignee | Status |
|---|-----------|-------------|----------|--------|
| 1 | **Google Calendar Integration** | Implement OAuth flow, retrieve free/busy times | Rebecca | 🔄 In Progress |
| 2 | **Calendly Integration** | Fetch availability from Calendly API | Emmanuel | 🔄 In Progress |
| 3 | **Outlook Calendar Support** | Research & plan Outlook API integration | Amir | ⏳ Planned |
| 4 | **Basic UI for Input** | Users can input Calendly/Google Calendar links | Rebecca | ✅ Done |
| 5 | **API Setup** | Backend endpoints for fetching availability | Emmanuel | 🔄 In Progress |
| 6 | **System Design** | High-level architecture & documentation | Amir | 🔄 In Progress |
| 7 | **Project Setup & Tracking** | Setup Trello board & initial repo structure | Emmanuel | ✅ Done |
| 8 | **Documentation** | RPM.md, Sprint1.md, ReadMe updates | Rebecca | 🔄 In Progress |

---

##  Task Breakdown  

### **Google Calendar Integration (Rebecca)**  
- ✅ Set up OAuth authentication flow  
- 🔄 Implement token exchange & storage  
- 🔄 Fetch user’s availability via Google Calendar API  
- ⏳ Format data for frontend display  

### **Calendly Integration (Emmanuel)**  
- ✅ Research Calendly API structure  
- 🔄 Implement availability retrieval from Calendly  
- ⏳ Format data for comparison  

### **Outlook Calendar Research (Amir)**  
- 🔄 Understand OAuth flow & API structure  
- ⏳ Identify key endpoints  

### **System Design (Amir)**  
- 🔄 Define high-level architecture  
- ⏳ Create diagrams for API & frontend-backend interaction  

### **Project Management (Emmanuel)**  
- ✅ Setup Trello for tracking tasks  
- 🔄 Update ReadMe with project details  

### **Documentation (Rebecca)**  
- ✅ Draft `RPM.md` (Release Planning)  
- 🔄 Document Sprint 1 (`sprint1.md`)  
- ⏳ Ensure up-to-date progress tracking  

---

## Team Capacity  

| Team Member | Available Hours (Sprint) | Primary Focus |
|-------------|-------------------------|---------------|
| Rebecca | 15-20 hrs | Google Calendar, Docs |
| Emmanuel | 15-20 hrs | Calendly, Backend API |
| Amir | 15-20 hrs | Outlook, System Design |

---

## Decisions Taken  
- **Prioritize Google Calendar and Calendly first**, as they are the most commonly used scheduling platforms.  
- **Outlook will be researched** but not implemented in this sprint.  
- **UI will remain minimal**, focusing on backend functionality first.  
- **Trello will be used for tracking** progress on tasks.  

---

##  Next Steps  
- Finalize OAuth authentication for Google, Outlook and Calendy Calendar  
- Retrieve availability from both Google, Outlook and Calendly APIs  
- Ensure backend endpoints return consistent formatted data  
- Track progress via Trello  

---

## Notes  
- Backend server issues were resolved by fixing endpoints.  
- OAuth callback correctly redirects users now.  
- library version conflicts resolved.  

