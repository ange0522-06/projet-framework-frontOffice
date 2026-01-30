# Spring Boot + Vue.js Framework

A full-stack application framework with Spring Boot backend and Vue.js frontend.

## Project Structure

```
projet-framework/
├── backend/          # Spring Boot REST API
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/framework/backend/
│   │   │   │   ├── config/         # Configuration classes
│   │   │   │   ├── controller/     # REST Controllers
│   │   │   │   ├── model/          # Entity models
│   │   │   │   ├── repository/     # JPA Repositories
│   │   │   │   ├── service/        # Business logic
│   │   │   │   └── BackendApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
└── frontend/         # Vue.js SPA
    ├── src/
    │   ├── components/   # Reusable components
    │   ├── views/        # Page components
    │   ├── router/       # Vue Router config
    │   ├── services/     # API services
    │   ├── App.vue
    │   └── main.js
    ├── index.html
    ├── vite.config.js
    └── package.json
```

## Technology Stack

### Backend
- **Spring Boot 3.2.1** - Java framework for building REST APIs
- **Spring Data JPA** - Database access layer
- **H2 Database** - In-memory database for development
- **Lombok** - Reduces boilerplate code
- **Maven** - Dependency management

### Frontend
- **Vue.js 3** - Progressive JavaScript framework
- **Vue Router 4** - Official router for Vue.js
- **Axios** - HTTP client for API calls
- **Vite** - Fast build tool and dev server

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Node.js 16+ and npm

### Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Build the project:
```bash
mvn clean install
```

3. Run the Spring Boot application:
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

#### API Endpoints

- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

#### H2 Database Console

Access the H2 console at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Run the development server:
```bash
npm run dev
```

The frontend will start on `http://localhost:5173`

4. Build for production:
```bash
npm run build
```

## Features

- ✅ RESTful API with Spring Boot
- ✅ CRUD operations example (User management)
- ✅ CORS configuration for frontend-backend integration
- ✅ Vue.js 3 with Composition API support
- ✅ Vue Router for navigation
- ✅ Axios for HTTP requests
- ✅ Responsive UI with forms and tables
- ✅ In-memory H2 database for quick development
- ✅ Hot reload for both frontend and backend development

## Development Workflow

1. Start the backend server (port 8080)
2. Start the frontend dev server (port 5173)
3. Access the application at `http://localhost:5173`
4. The frontend will proxy API requests to the backend

## Customization

### Adding New Entities

1. Create a new model in `backend/src/main/java/com/framework/backend/model/`
2. Create a repository in `backend/src/main/java/com/framework/backend/repository/`
3. Create a service in `backend/src/main/java/com/framework/backend/service/`
4. Create a controller in `backend/src/main/java/com/framework/backend/controller/`
5. Create corresponding views and services in the frontend

### Adding New Routes

1. Create a new view component in `frontend/src/views/`
2. Add the route in `frontend/src/router/index.js`
3. Add navigation link in `frontend/src/App.vue`

## License

ISC
