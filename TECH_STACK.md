# Tech Stack — Operis Guard

Visão geral de todas as tecnologias utilizadas no projeto.

---

## Linguagem & Runtime

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Java | 25 | Linguagem de programação orientada a objetos, fortemente tipada e amplamente utilizada no desenvolvimento de aplicações empresariais. Base de todo o projeto. |
| Spring Boot | 4.0.3 | Framework que simplifica a criação de aplicações Spring com configuração mínima. Fornece servidor embutido, auto-configuração e um ecossistema robusto de módulos. |

---

## Persistência

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| PostgreSQL | 16 | Sistema de gestão de base de dados relacional open-source, reconhecido pela sua robustez, conformidade com padrões SQL e suporte a tipos de dados avançados. |
| Spring Data JPA | - | Módulo do Spring que abstrai o acesso a dados relacionais, eliminando a necessidade de escrever queries SQL manualmente para operações comuns. |
| Hibernate ORM | 7.2.4 | Implementação da especificação JPA responsável pelo mapeamento entre objetos Java e tabelas relacionais, bem como pela geração e execução de SQL. |
| HikariCP | 7.0.2 | Pool de conexões de alto desempenho para JDBC. Gerencia e reutiliza conexões com a base de dados, reduzindo o custo de abertura de novas conexões a cada requisição. |

---

## Segurança

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Spring Security Crypto | - | Módulo do Spring focado em criptografia. Utilizado para encriptar palavras-passe com o algoritmo BCrypt antes de as armazenar na base de dados. |
| JWT (JSON Web Token) | - | Padrão aberto (RFC 7519) para transmissão segura de informações entre partes como um objeto JSON assinado. Utilizado para autenticação stateless com suporte ao contexto multi-tenant. |

---

## Infraestrutura

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Docker | 29.2.0 | Plataforma de containerização que permite empacotar e executar aplicações em ambientes isolados. Utilizado para correr o PostgreSQL localmente sem necessidade de instalação direta. |
| Docker Compose | - | Ferramenta para definir e gerir múltiplos containers Docker através de um único ficheiro de configuração YAML. Simplifica a orquestração do ambiente de desenvolvimento local. |

---

## Ferramentas de Desenvolvimento

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| IntelliJ IDEA | - | IDE da JetBrains amplamente utilizada no desenvolvimento Java/Kotlin. Oferece suporte avançado a refactoring, debugging, integração com Spring e ferramentas de produtividade. |
| Maven | - | Ferramenta de gestão de dependências e automação de build para projetos Java. Define a estrutura do projeto e as suas dependências através do ficheiro `pom.xml`. |
| Lombok | - | Biblioteca que reduz o código repetitivo (boilerplate) em Java através de anotações. Gera automaticamente getters, setters, construtores, builders e outros métodos em tempo de compilação. |
| Git | - | Sistema de controlo de versões distribuído. Permite rastrear alterações no código, colaborar em equipa e manter o histórico completo do projeto. |
| Gitflow | - | Estratégia de branching para Git que define um modelo claro de branches para features, releases e hotfixes, garantindo um fluxo de desenvolvimento organizado e seguro. |

---

## Monitoramento

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Spring Boot Actuator | - | Módulo do Spring Boot que expõe endpoints de monitoramento e gestão da aplicação. Permite verificar o estado de saúde da aplicação, métricas e informações do ambiente em tempo real. |

---