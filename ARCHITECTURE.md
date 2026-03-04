# Operis Platform — Architecture Overview

Visão geral da arquitetura de microserviços da plataforma Operis.

---

## Microservices Map
```
                        ┌─────────────────┐
                        │   API Gateway   │
                        │   (Traefik)     │
                        └────────┬────────┘
                                 │
                        ┌────────▼────────┐
                        │     GUARD       │
                        │  Auth & AuthZ   │
                        │  Users / Roles  │
                        │  Permissions    │
                        │  Companies      │
                        └────────┬────────┘
                                 │ JWT Token
          ┌──────────────────────┼──────────────────────┐
          │                      │                       │
┌─────────▼──────┐    ┌──────────▼──────┐    ┌─────────▼──────┐
│   TIMESHEET    │    │    PAYROLL      │    │   INVENTORY    │
│ Controle de    │    │ Gestão de       │    │ Estoque        │
│ Horas          │    │ Pagamentos      │    │ Descentralizado│
└────────────────┘    └─────────────────┘    └────────────────┘
          │                      │                       │
┌─────────▼──────┐    ┌──────────▼──────┐    ┌─────────▼──────┐
│   FLEET        │    │   WORKSITE      │    │   FINANCE      │
│ Gestão de      │    │ Canteiro de     │    │ Contas a Pagar │
│ Veículos       │    │ Obras / Agenda  │    │ e a Receber    │
└────────────────┘    └─────────────────┘    └────────────────┘
```

---

## Services

| Service | Repository | Responsibility |
|---------|------------|----------------|
| `guard` | `operis-guard` | Authentication, authorization, users, roles, permissions, companies |
| `timesheet` | `operis-timesheet` | Time tracking per user, per worksite, per period |
| `payroll` | `operis-payroll` | Payment calculation based on tracked hours |
| `inventory` | `operis-inventory` | Decentralized stock management per worksite |
| `fleet` | `operis-fleet` | Vehicle management and assignment |
| `worksite` | `operis-worksite` | Construction sites, schedules, phases, agenda |
| `finance` | `operis-finance` | Accounts payable and receivable |

---

## Common Standards

All services follow the same standards:

**Stack**
- Java 25 + Spring Boot 4
- PostgreSQL (dedicated database per service)
- Docker + Docker Compose
- Traefik (routing by hostname)
- Gitflow + CI/CD via GitHub Actions

**Authentication**
- All services validate the JWT issued by `guard`
- No service issues tokens — only `guard` does
- `companyId` is always extracted from the JWT, never from the request body

**Multi-tenancy**
- All data is filtered by `companyId` from the JWT
- Ensures full data isolation between companies

**Database isolation**
- Each service owns its database — no shared schemas
```
guard-db      → users, roles, permissions, companies
timesheet-db  → time entries, shifts
payroll-db    → payments, calculations
inventory-db  → items, movements, stock levels
fleet-db      → vehicles, assignments
worksite-db   → sites, schedules, phases
finance-db    → accounts, transactions
```

---

## Service Communication

**Synchronous (HTTP/REST)**
```
worksite  → timesheet   query hours per worksite
payroll   → timesheet   fetch hours for payment calculation
payroll   → finance     register completed payment
```

**Asynchronous (Message Broker — planned)**
```
timesheet → payroll     event: hours approved
worksite  → inventory   event: material requested
fleet     → finance     event: maintenance expense
```

---

## Development Roadmap

Services ordered by business dependency:

| Order | Service | Depends on |
|-------|---------|------------|
| 1 | `guard` | — |
| 2 | `worksite` | `guard` |
| 3 | `timesheet` | `guard`, `worksite` |
| 4 | `payroll` | `guard`, `timesheet` |
| 5 | `inventory` | `guard`, `worksite` |
| 6 | `fleet` | `guard` |
| 7 | `finance` | `guard`, `payroll`, `fleet` |

---
