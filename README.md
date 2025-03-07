# MeetMerge

As our lives get busier, many of us need schedules to make sure we complete our work and complete our goals on time. However, even those schedules can quickly become disorganized! MeetMerge is a web application that helps coordinate meetings by merging multiple Calendly or Google Calendar share links to determine overlapping availability. It can generate a new Calendly event or even autofill a LettuceMeet event based on the merged schedule.

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

## Run the Spring Boot Application:

### Using the Maven Wrapper:
```bash
./mvnw spring-boot:run
```

### Or if you have Maven installed:
```bash
mvn spring-boot:run
```

## Test the Backend:
Open your browser and visit:
```bash
http://localhost:8080/api/meetings/test
```
You should see a message like:
```arduino
MeetMerge Backend is running!
```

## Frontend Setup (Vite + React)

### Navigate to the Frontend Directory:
```bash
cd ../frontend
```

### Install Dependencies:
```bash
npm install
```
Or if you use Yarn:
```bash
yarn install
```

### Start the Development Server:
```bash
npm run dev
```
Or with Yarn:
```bash
yarn dev
```
The frontend should now be running (typically at [http://localhost:5173](http://localhost:5173)).

## How It Works

### User Interface:
Users paste their Calendly or Google Calendar share links into the app.

### Backend Processing:
The Spring Boot backend receives the links, processes them to determine overlapping availability, and can generate a new event link.

### Communication:
The React frontend communicates with the backend using Axios. Ensure CORS is correctly configured in your Spring Boot controllers if the frontend and backend are running on different ports.

## Configuration

### CORS:
The backend uses the `@CrossOrigin` annotation on controllers to allow requests from your frontend (e.g., [http://localhost:5173](http://localhost:5173)). Adjust this as needed.

### Database Integration (Optional):
You can add Spring Data JPA with PostgreSQL or MySQL later by updating the `pom.xml` and configuring your `application.properties`.

### Security (Optional):
Spring Boot Security can also be integrated later. A basic security configuration can be added to secure your endpoints.

## Troubleshooting

### Maven Wrapper Issues:
If you encounter errors related to missing Maven wrapper files (like `.mvn/wrapper/maven-wrapper.properties`), ensure that the `.mvn` directory isn’t excluded by your `.gitignore`. You may need to force-add it:
```bash
git add -f .mvn
```

### CORS Errors:
Verify the `@CrossOrigin` configuration on your controllers to ensure requests from your frontend are permitted.

### API Errors:
If the frontend displays "Error fetching data", check that your backend is running and that the endpoint URL is correct. Use your browser or a tool like Postman to verify that:
```bash
http://localhost:8080/api/meetings/test
```
returns the expected response.

### Contributing:
Contributions are welcome! If you see an issue that you can solve, first fork this repository on Github.
Then clone this repository:
```bash
git clone https://github.com/hvpham-yorku/project-meetmerge/ 
```
Next, create a new branch:
```bash
git checkout -b solutionbranch
```
Prepare the changes:
```bash
git add .
```
Make your changes and commit with a clear message:
```bash
git commit - m "Fixed problem E"
```
Push to the branch you previously created:
```bash
git push origin solutionbranch
```
Create a pull request so we can review.

### Team:
Rebecca John
Emmanuel Ideho
Amir Dmore


