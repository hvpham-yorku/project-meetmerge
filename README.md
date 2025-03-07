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

## Contributing

Contributions are welcome! Feel free to open issues or submit pull requests with improvements, bug fixes, or new features.

## License

This project is licensed under the MIT License.

## Contact

For questions or support, please contact [your-email@example.com] or [your GitHub username].

---

This README file provides an overview of MeetMerge, explains the project structure, details setup instructions for both the backend and frontend, and includes troubleshooting, contribution, and licensing information. Adjust the repository URL, contact details, and any other sections as needed for your project.

