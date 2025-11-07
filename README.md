# ConnectAmong
Domain-scoped community platform where users connect only with people from their own domain (e.g., STUDENT, TEACHER, DEVELOPER, DESIGNER).

## Features
- **Authentication**: JWT-based login/register with domain selection
- **Domain Wall**: Users can only see and interact with content from their own domain
- **Feed**: View and create posts, react, and comment
- **Groups**: Create and join groups within your domain
- **Jobs**: Post job listings and apply to jobs in your domain
- **Search**: Search for users and posts within your domain
- **Profile Management**: Update your profile information

## Tech Stack
### Backend
- Java 17
- Spring Boot 3
- MySQL
- Flyway (database migrations)
- JWT for authentication
- Swagger UI for API documentation

### Frontend
- React
- TypeScript
- Vite
- Tailwind CSS
- React Router
- Axios

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven
- Node.js and npm
- MySQL 8.0

### Backend Setup

1. **Start MySQL**
   Make sure MySQL is running on localhost:3306

2. **Configure Database**
   Update credentials in `backend/src/main/resources/application.yml` if needed:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/connectamong?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
       username: root
       password: Anish@MySQL1
   ```

3. **Important: Update JWT Secret**
   Before running in production, change the JWT secret in `application.yml`:
   ```yaml
   app:
     jwt:
       secret: "your-very-long-random-secret-64+chars-minimum"
   ```

4. **Run Backend**
   ```bash
   cd backend
   mvn clean spring-boot:run
   ```

5. **Access Swagger UI**
   Open browser: http://localhost:8080/swagger-ui/index.html

### Frontend Setup

1. **Install Dependencies**
   ```bash
   cd frontend
   npm install
   ```

2. **Run Frontend**
   ```bash
   npm run dev
   ```

3. **Access Frontend**
   Open browser: http://localhost:5173

## Test Users (Seeded Data)

| Email | Password | Domain | Description |
|-------|----------|--------|-------------|
| alice@student.com | Pass@123 | STUDENT | CS undergrad |
| bob@teacher.com | Pass@123 | TEACHER | CS teacher |
| dev@developer.com | Pass@123 | DEVELOPER | Backend developer |

## Available Domains
- STUDENT
- TEACHER
- DEVELOPER
- DESIGNER
- DATA_SCIENTIST
- PRODUCT
- MARKETING

## API Endpoints

### Public Endpoints
- POST `/api/auth/register` - Register new user
- POST `/api/auth/login` - Login
- GET `/api/domains` - Get list of available domains (searchable)

### Protected Endpoints (require JWT)
- GET `/api/me` - Get current user profile
- PUT `/api/me` - Update profile
- GET `/api/feed` - Get posts from your domain
- POST `/api/posts` - Create a post
- GET `/api/posts/{id}` - Get post details
- POST `/api/posts/{id}/comments` - Add comment
- POST `/api/posts/{id}/react` - React to post
- DELETE `/api/posts/{id}/react` - Remove reaction
- POST `/api/groups` - Create group
- GET `/api/groups` - List groups in your domain
- POST `/api/groups/{id}/join` - Join group
- DELETE `/api/groups/{id}/leave` - Leave group
- GET `/api/groups/{id}/posts` - Get group posts
- POST `/api/jobs` - Post a job
- GET `/api/jobs` - List jobs in your domain
- POST `/api/jobs/{id}/apply` - Apply to job
- GET `/api/jobs/my-applications` - Get your applications
- GET `/api/search/posts?q=` - Search posts
- GET `/api/search/users?q=` - Search users

## Domain Enforcement

The platform enforces strict domain boundaries:
- Users can only see posts, groups, and jobs from their own domain
- Cross-domain interactions return 403 Forbidden
- All content is automatically scoped to the user's domain

## Development Notes

- Backend runs on port 8080
- Frontend runs on port 5173 (Vite default)
- CORS is configured to allow requests from localhost:5173 and localhost:3000
- Database migrations are handled by Flyway automatically
- Seed data is loaded on first run

## Project Structure

```
ConnectAmong/
├── backend/
│   ├── src/main/java/com/connectamong/
│   │   ├── auth/           # Authentication & JWT
│   │   ├── users/          # User management
│   │   ├── posts/          # Posts, comments, reactions
│   │   ├── groups/         # Groups & members
│   │   ├── jobs/           # Jobs & applications
│   │   ├── search/         # Search functionality
│   │   ├── config/         # Security, CORS, OpenAPI
│   │   └── common/         # Utilities & error handling
│   └── src/main/resources/
│       ├── application.yml
│       └── db/migration/   # Flyway SQL scripts
└── frontend/
    └── src/
        ├── api/            # Axios setup
        ├── auth/           # Auth context
        ├── components/     # Reusable components
        ├── pages/          # Page components
        └── routes/         # Protected routes
```

## License
MIT
