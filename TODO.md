# TODO List — Operis Guard

## ✅ Done

### Infrastructure & Configuration
- [x] Spring Boot 4 project setup
- [x] PostgreSQL 16 via Docker Compose
- [x] Gitflow branching strategy
- [x] Semantic commits
- [x] Spring Security (stateless)
- [x] RSA key pair generation (local only, never committed)
- [x] Environment profiles (dev, prod) via Spring Profiles
- [x] application.yml split into base, application-dev.yml and application-prod.yml
- [x] CORS configuration per environment via `app.cors.allowed-origins`
- [x] Docker image for the application (Dockerfile)
- [x] Docker Compose with dev override (docker-compose.override.yml)
- [x] Traefik reverse proxy with automatic TLS (Let's Encrypt)
- [x] CI/CD pipeline (GitHub → VPS)

### Domain Model
- [x] User entity
- [x] Company entity (EU — BE, PT, etc.)
- [x] Role entity (BASE and CUSTOM)
- [x] Permission entity
- [x] UserCompany entity (multi-tenant link)
- [x] RefreshToken entity

### Security
- [x] BCrypt password encryption
- [x] JWT authentication with RS256 (Nimbus JOSE)
- [x] Access Token (15 min) + Refresh Token rotation (7 days)
- [x] Public UUID on all entities (internal IDs never exposed)
- [x] JwtAuthenticationFilter (skips public routes)
- [x] SecurityFilterChain with CSRF disabled and stateless session

### Data Seed
- [x] DataInitializer (`@Profile("!prod")`) — runs only in non-prod environments
- [x] 17 base permissions seeded automatically
- [x] ADMIN role (BASE) with all permissions
- [x] Default company (Operis) seeded
- [x] Admin user linked to company with ADMIN role

### API Endpoints
- [x] POST /api/v1/auth/login
- [x] POST /api/v1/auth/refresh
- [x] POST /api/v1/auth/logout
- [x] POST /api/v1/users
- [x] GET /api/v1/users
- [x] GET /api/v1/users/{id}
- [x] PATCH /api/v1/users/{id}/status
- [x] POST /api/v1/companies
- [x] GET /api/v1/companies
- [x] GET /api/v1/companies/{id}
- [x] GET /api/v1/companies/registration/{number}
- [x] GET /api/v1/companies/search?name={name}
- [x] PUT /api/v1/companies/{id}
- [x] PATCH /api/v1/companies/{id}/status
- [x] POST /api/v1/permissions
- [x] GET /api/v1/permissions
- [x] GET /api/v1/permissions/{id}
- [x] PUT /api/v1/permissions/{id}
- [x] DELETE /api/v1/permissions/{id}
- [x] POST /api/v1/roles
- [x] GET /api/v1/roles
- [x] GET /api/v1/roles/{id}
- [x] GET /api/v1/roles/type/{type}
- [x] GET /api/v1/roles/company/{companyId}
- [x] PUT /api/v1/roles/{id}
- [x] POST /api/v1/roles/{id}/permissions
- [x] DELETE /api/v1/roles/{id}/permissions
- [x] DELETE /api/v1/roles/{id}

### Documentation
- [x] README.md
- [x] TECH_STACK.md
- [x] TODO.md

---

## 🔲 Backlog

### API & Validation
- [ ] Global exception handler (`@RestControllerAdvice`)
- [ ] Input validation (Bean Validation / Jakarta)
- [ ] GET /api/v1/auth/me endpoint

### Domain
- [ ] UserProfile entity (personal data)
- [ ] UserCompany CRUD endpoints
- [ ] Role-based access control on endpoints (`@PreAuthorize`)

### Quality
- [ ] Unit tests (UserService, AuthService, JwtService)
- [ ] Integration tests
- [ ] API documentation (Swagger / OpenAPI)

### Infrastructure
- [ ] Centralized logging (ELK or similar)

---
