# Guard — User Management Service

Microservice responsible for user management within the Operis platform.

---

## Tech Stack

- Java 21
- Spring Boot
- Maven
- Docker (soon)

---

## Gitflow

This project follows the [Gitflow](https://nvie.com/posts/a-successful-git-branching-model/) branching strategy.

### Branch Structure

| Branch       | Purpose                              |
|--------------|--------------------------------------|
| `main`       | Production-ready code                |
| `develop`    | Integration branch for development   |
| `feature/*`  | New features, branched from develop  |
| `release/*`  | Release preparation, from develop    |
| `hotfix/*`   | Urgent fixes, branched from main     |

### Workflow

**Starting a new feature:**
```bash
git checkout develop
git checkout -b feature/your-feature-name
```

**Finishing a feature:**
```bash
git checkout develop
git merge --no-ff feature/your-feature-name
git branch -d feature/your-feature-name
git push origin develop
```

**Creating a release:**
```bash
git checkout develop
git checkout -b release/x.x.x
# bump version, final adjustments
git checkout main
git merge --no-ff release/x.x.x
git tag -a vx.x.x -m "Release x.x.x"
git checkout develop
git merge --no-ff release/x.x.x
git branch -d release/x.x.x
```

**Creating a hotfix:**
```bash
git checkout main
git checkout -b hotfix/description
# fix the issue
git checkout main
git merge --no-ff hotfix/description
git checkout develop
git merge --no-ff hotfix/description
git branch -d hotfix/description
```

---

## Running Locally
```bash
./mvnw spring-boot:run
```

Health check:
```bash
curl http://localhost:8080/actuator/health
```



## Project Structure
```
src/main/java/com/operis/guard/
├── GuardApplication.java
├── controller/    # REST controllers, request/response handling
├── service/       # Business logic
├── repository/    # Data access layer (Spring Data JPA)
└── entity/        # JPA entities mapped to database tables
```


## API Endpoints

### Users — `/api/v1/users`

| Method  | Endpoint                  | Description                        |
|---------|---------------------------|------------------------------------|
| POST    | `/api/v1/users`           | Create a new user                  |
| GET     | `/api/v1/users`           | List all users (TODO: filter by company) |
| GET     | `/api/v1/users/{id}`      | Find user by ID                    |
| PATCH   | `/api/v1/users/{id}/status` | Update user active status |


> Internal operations (findByEmail, delete) are available at the service layer only.

### Companies — `/api/v1/companies`

| Method | Endpoint                                    | Description                        |
|--------|---------------------------------------------|------------------------------------|
| POST   | `/api/v1/companies`                         | Create a new company               |
| GET    | `/api/v1/companies`                         | List all companies                 |
| GET    | `/api/v1/companies/{id}`                    | Find company by ID                 |
| GET    | `/api/v1/companies/registration/{number}`   | Find company by registration number|
| GET    | `/api/v1/companies/search?name={name}`      | Search companies by name           |
| PUT    | `/api/v1/companies/{id}`                    | Update company data                |
| PATCH  | `/api/v1/companies/{id}/status`             | Update company active status       |


## Security Context

This service handles authentication and authorization in a **multi-tenant** architecture:

- A user can be linked to multiple companies
- Each link carries a specific role within that company
- JWT tokens will carry the active company and role context


## Multi-Tenant Model

This service supports a multi-tenant architecture where a user can be linked
to multiple companies, each with a different role.

### Entity Relationship
```
users ──── user_company ──── company
                │
               role ──── role_permission ──── permission
```

### Roles

| Type   | Description                                      |
|--------|--------------------------------------------------|
| BASE   | Global roles available to all companies          |
| CUSTOM | Roles created and managed by a specific company  |