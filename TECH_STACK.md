# Tech Stack — Operis Guard

Visão geral de todas as tecnologias utilizadas no projeto.

---

## Linguagem & Runtime

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Java | 25 | Linguagem base do projeto. |
| Spring Boot | 4.0.3 | Framework principal com auto-configuração, servidor embutido e ecossistema robusto. |

---

## Persistência

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| PostgreSQL | 16 | Base de dados relacional principal. |
| Spring Data JPA | — | Abstração de acesso a dados relacionais. |
| Hibernate ORM | 7.2.4 | Mapeamento objeto-relacional e geração de SQL. |
| HikariCP | 7.0.2 | Pool de conexões de alto desempenho para JDBC. |

---

## Segurança

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Spring Security | — | Framework de autenticação e autorização. Gestão do `SecurityFilterChain`, CSRF, sessão stateless e filtros. |
| Spring Security Crypto | — | Encriptação de passwords com BCrypt. |
| JWT (RS256) | — | Tokens assinados com par de chaves RSA. O access token carrega o contexto multi-tenant completo (empresa, role, permissões). |
| Nimbus JOSE + JWT | — | Biblioteca para geração, assinatura e validação de JWTs com chaves RSA PEM. |

---

## Infraestrutura

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Docker | 29.2.0 | Containerização da aplicação e base de dados. |
| Docker Compose | — | Orquestração local e em produção. Perfis separados via `override` para dev e `docker-compose.yml` base para prod. |
| Traefik | — | Reverse proxy em produção. Gestão automática de TLS via Let's Encrypt e roteamento por hostname. |
| VPS (Linux) | — | Servidor de produção onde os containers correm via Docker Compose. |

---

## Configuração & Perfis

| Tecnologia | Descrição |
|------------|-----------|
| Spring Profiles | Separação de configuração entre `dev` e `prod` via `application-dev.yml` e `application-prod.yml`. |
| Environment Variables | Credenciais e configurações sensíveis injetadas via `.env` (nunca commitado). |
| ConfigurationProperties | `CorsProperties` para tipagem segura das origens permitidas por ambiente. |

---

## Ferramentas de Desenvolvimento

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| IntelliJ IDEA | — | IDE principal para desenvolvimento Java. |
| Maven | — | Gestão de dependências e automação de build via `pom.xml`. |
| Lombok | — | Redução de boilerplate via anotações (`@Builder`, `@Data`, `@RequiredArgsConstructor`, etc.). |
| Git | — | Controlo de versões distribuído. |
| Gitflow | — | Estratégia de branching com branches `main`, `develop`, `feature/*`, `fix/*`, `hotfix/*`, `release/*`. |

---

## Monitoramento

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Spring Boot Actuator | — | Endpoints de saúde e métricas expostos em `/actuator/health` e `/actuator/info`. |
