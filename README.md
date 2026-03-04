# Guard — Authentication & Authorization Service

Microservice responsible for authentication and authorization within the Operis platform.

---

## Tech Stack

- Java 25
- Spring Boot 4.0.3
- Maven
- Docker + Docker Compose
- PostgreSQL 16
- Spring Security + JWT (RS256)
- Traefik (reverse proxy + TLS)

---

## Environments

| Profile | Usage |
|---------|-------|
| `dev` | Local — banco exposto, seed ativo, logs verbose |
| `prod` | VPS — banco isolado, seed desativado, Traefik |

### Rodando local (IDE)
```bash
# Sobe só o banco
docker compose up -d guard-db

# Na IDE, setar env var:
SPRING_PROFILES_ACTIVE=dev
```

### Rodando local (Docker completo)
```bash
docker compose up -d
docker logs guard -f
```

### Rodando em produção (VPS)
```bash
docker compose -f docker-compose.yml up -d --build
```

Health check:
```bash
curl https://api.gustavohub.com/actuator/health
```

---

## Environment Variables

Copie `.env.example` para `.env` e preencha. **Nunca commite o `.env`.**
```bash
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:postgresql://guard-db:5432/guarddb
DB_NAME=guarddb
DB_USER=guard_user
DB_PASSWORD=your_password
DDL_AUTO=validate
JWT_PRIVATE_KEY_PATH=file:/app/certs/private.pem
JWT_PUBLIC_KEY_PATH=file:/app/certs/public.pem
JWT_ACCESS_TOKEN_EXPIRATION=900
JWT_REFRESH_TOKEN_EXPIRATION=604800
ADMIN_EMAIL=admin@guard.io
ADMIN_PASSWORD=your_secure_password
```

---

## Certificates (JWT RS256)

| Ambiente | Localização |
|----------|-------------|
| Dev | `src/main/resources/certs/` |
| Prod | `~/certs/guard/` na VPS → montado em `/app/certs/` no container |

---

## Data Seed

O `DataInitializer` roda automaticamente **apenas em perfis não-prod** (`@Profile("!prod")`).

Cria automaticamente:
- 17 permissões base
- Role `ADMIN` (tipo `BASE`) com todas as permissões
- Empresa padrão (`Operis`)
- Utilizador admin vinculado à empresa com role ADMIN

**Primeiro deploy em produção:**
```bash
# 1. No .env da VPS, muda temporariamente
SPRING_PROFILES_ACTIVE=dev
DDL_AUTO=update

# 2. Sobe
docker compose up -d --build

# 3. Confirma o seed nos logs, depois reverte
SPRING_PROFILES_ACTIVE=prod
DDL_AUTO=validate
docker compose up -d --build
```

---

## Gitflow

| Branch | Purpose |
|--------|---------|
| `main` | Production-ready code |
| `develop` | Integration branch |
| `feature/*` | New features |
| `release/*` | Release preparation |
| `hotfix/*` | Urgent fixes from main |
| `fix/*` | Bug fixes from develop |
```bash
# Nova feature
git checkout develop
git checkout -b feature/your-feature-name

# Finalizar feature
git checkout develop
git merge --no-ff feature/your-feature-name
git branch -d feature/your-feature-name
git push origin develop
```

---

## Project Structure
```
src/main/java/com/operis/guard/
├── GuardApplication.java
├── config/            # Security, CORS, JWT, DataInitializer
├── controller/        # REST controllers
├── service/           # Business logic
├── repository/        # Spring Data JPA
└── entity/            # JPA entities
    └── enumerator/    # RoleType, etc.

src/main/resources/
├── application.yml          # Base (compartilhado)
├── application-dev.yml      # Overrides dev
├── application-prod.yml     # Overrides prod
└── certs/                   # RSA keys (dev only)
```

---

## Authentication Flow

1. `POST /api/v1/auth/login` — autentica com email + password, retorna tokens com primeira empresa (ordem alfabética) como contexto padrão
2. `POST /api/v1/auth/switch-company` — muda contexto de empresa ativa, rotaciona refresh token
3. `POST /api/v1/auth/refresh` — renova access token
4. `POST /api/v1/auth/logout` — revoga refresh token

### Token Structure
```json
{
  "sub": "user-public-id",
  "email": "user@email.com",
  "companyId": "company-public-id",
  "role": "ADMIN",
  "permissions": ["CREATE_USER", "READ_USER"]
}
```

### Token Lifecycle

| Token | Expiração | Descrição |
|-------|-----------|-----------|
| Access Token | 15 minutos | Short-lived, carrega contexto de auth |
| Refresh Token | 7 dias | Long-lived, armazenado na DB, rotacionado a cada uso |

---

## API Endpoints

### Auth — `/api/v1/auth`
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/login` | Authenticate and receive tokens |
| POST | `/api/v1/auth/refresh` | Refresh access token |
| POST | `/api/v1/auth/switch-company` | Switch active company context |
| POST | `/api/v1/auth/logout` | Revoke refresh token |

### Users — `/api/v1/users`
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/users` | Create a new user |
| GET | `/api/v1/users` | List all users |
| GET | `/api/v1/users/{id}` | Find user by public ID |
| PATCH | `/api/v1/users/{id}/status` | Update user active status |

### Companies — `/api/v1/companies`
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/companies` | Create a new company |
| GET | `/api/v1/companies` | List all companies |
| GET | `/api/v1/companies/{id}` | Find company by public ID |
| GET | `/api/v1/companies/registration/{number}` | Find by registration number |
| GET | `/api/v1/companies/search?name={name}` | Search by name |
| PUT | `/api/v1/companies/{id}` | Update company data |
| PATCH | `/api/v1/companies/{id}/status` | Update active status |

### Permissions — `/api/v1/permissions`
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/permissions` | Create a new permission |
| GET | `/api/v1/permissions` | List all permissions |
| GET | `/api/v1/permissions/{id}` | Find by public ID |
| PUT | `/api/v1/permissions/{id}` | Update permission |
| DELETE | `/api/v1/permissions/{id}` | Delete permission |

### Roles — `/api/v1/roles`
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/roles` | Create a new role |
| GET | `/api/v1/roles` | List all roles |
| GET | `/api/v1/roles/{id}` | Find by public ID |
| GET | `/api/v1/roles/type/{type}` | List by type (BASE/CUSTOM) |
| GET | `/api/v1/roles/company/{id}` | List by company |
| PUT | `/api/v1/roles/{id}` | Update role |
| POST | `/api/v1/roles/{id}/permissions` | Add permission to role |
| DELETE | `/api/v1/roles/{id}/permissions` | Remove permission from role |
| DELETE | `/api/v1/roles/{id}` | Delete role |

---

## Security

### Public Identifiers
| Field | Type | Description |
|-------|------|-------------|
| `id` | Long | Internal — never exposed |
| `publicId` | UUID | Used in all API responses and JWT |

### CORS
Allowed origins configurados por ambiente via `app.cors.allowed-origins` no yml do perfil correspondente.

### JWT Filter
O `JwtAuthenticationFilter` ignora rotas públicas (`/api/v1/auth/**`, `/actuator/**`) e valida o Bearer token em todas as outras. Token inválido em rota protegida → `401 Unauthorized`.

---

## Multi-Tenant Model
```
users ──── user_company ──── company
                │
               role ──── role_permission ──── permission
```

| Role Type | Description |
|-----------|-------------|
| `BASE` | Global, disponível para todas as empresas |
| `CUSTOM` | Criada e gerida por uma empresa específica |

---

## Infrastructure

| Ficheiro | Uso |
|----------|-----|
| `docker-compose.yml` | Base — produção |
| `docker-compose.override.yml` | Dev — aplicado automaticamente local |

Traefik em produção: `api.gustavohub.com` com TLS automático via Let's Encrypt.
