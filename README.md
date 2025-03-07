# MeetMerge

MeetMerge is a web application that helps coordinate meetings by merging multiple Calendly or Google Calendar share links to determine overlapping availability. It can generate a new Calendly event or even autofill a LettuceMeet event based on the merged schedule.

## Features

- **Calendar Merging:** Paste multiple calendar share links to find overlapping available times.
- **Event Generation:** Automatically generate a new Calendly event or autofill a LettuceMeet event.
- **Modern Stack:** 
  - **Frontend:** Built with Vite and React for fast development and a smooth user experience.
  - **Backend:** Powered by Java Spring Boot, providing robust RESTful endpoints.

## Tech Stack

- **Frontend:** React with Vite
- **Backend:** Java Spring Boot
- **Build Tools:** Maven (with Maven Wrapper) for backend and Node.js/npm (or Yarn) for frontend

## Prerequisites

- **Java:** JDK 17 or later
- **Maven:** Either installed globally or use the Maven Wrapper provided
- **Node.js & npm/Yarn:** For managing the frontend dependencies

## Getting Started

### Backend Setup (Spring Boot)

1. **Clone the Repository & Navigate to the Backend:**
   ```bash
   git clone https://github.com/yourusername/MeetMerge.git
   cd MeetMerge/backend

2.**Run the Spring Boot Application:**
-Using the Maven Wrapper:
