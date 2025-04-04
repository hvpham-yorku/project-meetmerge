# Product Backlog  
## MeetMerge: Calendar Availability Combiner  

After discussing with my team, we've put together these user stories for our calendar combiner app. We tried to cover all the essential features we'll need plus some nice-to-have stuff for later sprints.  

---

## **Core User Stories**  

### 1. Link Input  
- **As a user,** I want to paste multiple Calendly links into the app so I can find when everyone is free without manually checking each calendar.  
#### Criteria for satisfaction:  
  - The interface should let me paste at least **5 different Calendly links**.  
  - The system should **check if the links are valid Calendly URLs**.  
  - I should get **an error message** if I paste an invalid link.  
  - Should be able to **add links one by one or all at once**.  

### 2. Google Calendar Support ✅ (Completed)  
- **As a user,** I want to paste Google Calendar links too, since not everyone uses Calendly.  
#### Criteria for satisfaction:  
  - System needs to **work with Google Calendar sharing links**.  
  - It should correctly **pull availability info** from these Google Calendar links.  
  - Users should be able to **mix Calendly and Google Calendar links together**.  

### 3. Calendly API Integration ✅ (Completed)  
- **As a user,** I want the system to integrate with Calendly's API to pull availability data more efficiently.  
#### Criteria for satisfaction:  
  - System should **authenticate with Calendly API** to retrieve availability.  
  - Calendly availability should be **fetched directly** instead of relying only on shared links.  
  - The integration should **handle errors gracefully** if Calendly's API is down.  

### 4. Yahoo Calendar Integration (Replacing Outlook API) 🚧 (In Progress)  
- **As a user,** I want to integrate my Yahoo Calendar since we are moving away from Outlook API due to technical issues.  
#### Criteria for satisfaction:  
  - System should **connect to Yahoo Calendar API** to pull availability.  
  - It should allow users to **mix Yahoo Calendar with Google Calendar and Calendly**.  
  - The integration should handle **time zone differences**.  

### 5. Finding Overlapping Times  
- **As a user,** I want the app to show me when everyone is available at the same time.  
#### Criteria for satisfaction:  
  - System needs to **accurately find times** that work for everyone.  
  - It should handle **different time zones correctly**.  
  - Results should clearly **show the date and time ranges** that work.  

### 6. Showing Results Clearly  
- **As a user,** I want to see the available times in an easy-to-understand format.  
#### Criteria for satisfaction:  
  - Results should **look like a calendar view**.  
  - Times should be displayed in **my local time zone**.  
  - If there are **no overlapping times**, it should tell me that.  

### 7. Creating a New Calendly Link  
- **As a user,** I want to generate a new Calendly link with just the overlapping times so I can send one link to everyone.  
#### Criteria for satisfaction:  
  - System **creates a new Calendly event** showing only times when everyone is free.  
  - The link should actually **work when people click it**.  
  - I should be able to **give the event a custom name and description**.  

### 8. Lettucemeet Integration  
- **As a user,** I want to create a Lettucemeet poll with the overlapping times since some people prefer that.  
#### Criteria for satisfaction:  
  - System should **pre-fill a Lettucemeet poll** with the times that work for everyone.  
  - The link should **work correctly**.  
  - I should be able to **add a title and description** for the poll.  

---

## **Sprint Planning Update**  
We **completed Google Calendar and Calendly API integrations**, but **Outlook API had too many issues**. Instead, we are now **working on Yahoo Calendar integration** as a replacement. Our focus in the next sprint will be **finalizing Yahoo integration and improving the overlap detection algorithm**.  
