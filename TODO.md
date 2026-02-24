# TODO List — Operis Guard

## ✅ Done

### Infrastructure & Configuration
- [x] Spring Boot 4 project setup
- [x] PostgreSQL 16 via Docker Compose
- [x] Gitflow branching strategy
- [x] Semantic commits
- [x] application.yaml configuration
- [x] Spring Security (stateless)
- [x] RSA key pair generation (local only, never committed)

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

### API Endpoints
- [x] POST /auth/login
- [x] POST /auth/refresh
- [x] POST /auth/logout
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

---

## 🔲 Backlog

### API & Validation
- [ ] Global exception handler (ExceptionHandler)
- [ ] Input validation (Bean Validation / Jakarta)
- [ ] GET /auth/me endpoint

### Domain
- [ ] UserProfile entity (personal data)
- [ ] UserCompany CRUD endpoints
- [ ] Role-based access control on endpoints (@PreAuthorize)

### Quality
- [ ] Unit tests (UserService, AuthService, JwtService)
- [ ] Integration tests
- [ ] API documentation (Swagger / OpenAPI)

### Infrastructure
- [ ] Docker image for the application
- [ ] Environment profiles (dev, prod)
- [ ] Centralized logging

---

> This file is updated at the end of each development cycle.