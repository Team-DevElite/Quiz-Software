# Quizzer

Quizzer is an educational quiz platform developed by **Team DevElite** for course SOF005AS3AE. This repository contains the Java backend, a server-rendered teacher dashboard, and a quiz REST API. A React/Vite student dashboard is planned and is not included in this repository yet.

## Current features

- Create, list, edit, publish/unpublish, and delete quizzes through the teacher dashboard.
- View the questions belonging to a quiz.
- List, add, and delete answer options for existing questions.
- Create, list, and delete quizzes through the REST API.
- Persist quizzes, questions, and answer options with Spring Data JPA. Deleting a quiz cascades to its questions and answer options.

Question creation/deletion pages, answer option editing, the student frontend, and demo data seeding are still pending. The application starts with an empty in-memory database.

## Tech stack and structure

| Component | Technology |
|---|---|
| Language | Java 21 (Maven compilation target) |
| Framework | Spring Boot 3.4.13 |
| Teacher dashboard | Spring MVC and Thymeleaf |
| Persistence | Spring Data JPA and Hibernate |
| Local database | H2, in memory |
| Build | Maven Wrapper |
| Tests | JUnit 5, Spring Boot Test, and MockMvc |
| Container | Multi-stage Docker build using Java 25 images |

```text
src/main/java/com/DevElite/Quiz/
  domain/       JPA entities and repositories
  service/      Quiz and answer option business logic
  web/          MVC and REST controllers
src/main/resources/
  templates/    Thymeleaf teacher dashboard
  application.properties
  application-rahti.properties
src/test/java/com/DevElite/Quiz/
  QuizApplicationTests.java
```

The domain hierarchy is `Quiz → Question → AnswerOption`. MVC controllers render HTML; the REST controller returns JSON. The entity collections for questions and answer options use `@JsonIgnore`, so quiz JSON does not include nested questions or answer options.

## Run locally

Install **JDK 21 or a compatible newer JDK** and Git. Maven is downloaded by the wrapper on first use; a separate Maven installation is not required. Check your Java installation with `java -version`.

```bash
git clone https://github.com/Team-DevElite/Quiz-Software.git
cd Quiz-Software
```

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

| Service | Local URL |
|---|---|
| Teacher dashboard | http://localhost:8080/ |
| Quiz list | http://localhost:8080/quizzes |
| REST API | http://localhost:8080/api/quizzes |
| H2 console | http://localhost:8080/h2-console |

Use these credentials in the H2 console:

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: leave blank

Local data is lost when the application stops. To use another HTTP port, run `./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081` and adjust the URLs accordingly.

## Teacher dashboard routes

| Method | Path | Purpose |
|---|---|---|
| GET | `/` or `/quizzes` | List quizzes |
| GET / POST | `/quizzes/add` | Display / submit quiz creation form |
| GET / POST | `/quizzes/edit/{id}` | Display / submit quiz edit form |
| GET | `/quizzes/delete/{id}` | Delete a quiz and its children |
| GET | `/quizzes/{quizId}/questions` | List a quiz's questions |
| GET | `/questions/{questionId}/options` | List answer options |
| GET / POST | `/questions/{questionId}/options/add` | Display / submit answer option form |
| GET | `/options/delete/{id}` | Delete an answer option |

## REST API

| Method | Path | Behavior |
|---|---|---|
| GET | `/api/quizzes` | Return all quizzes, including unpublished quizzes |
| POST | `/api/quizzes` | Save a quiz and return it with HTTP 201 |
| DELETE | `/api/quizzes/{id}` | Delete a quiz; HTTP 204 on success or 404 if missing |

Create a quiz from a macOS/Linux terminal:

```bash
curl -X POST http://localhost:8080/api/quizzes \
  -H 'Content-Type: application/json' \
  -d '{"name":"Java Fundamentals","description":"Review Java basics","courseCode":"JAVA101","isPublished":false}'
```

List quizzes:

```bash
curl http://localhost:8080/api/quizzes
```

Published-only filtering and cross-origin access for the planned student frontend are not wired into the REST controller yet. Setting a quiz's publication flag currently does not hide it from `GET /api/quizzes`.

## Tests and packaging

On macOS/Linux:

```bash
./mvnw test
./mvnw clean package
java -jar target/Quiz-0.0.1-SNAPSHOT.jar
```

On Windows PowerShell:

```powershell
.\mvnw.cmd test
.\mvnw.cmd clean package
java -jar target/Quiz-0.0.1-SNAPSHOT.jar
```

The existing integration suite covers application startup, quiz persistence and deletion, entity relationships and cascade deletion, repository queries, and quiz dashboard routes.

## Containers and Rahti configuration

Build and run the local H2 configuration with Docker:

```bash
docker build -t quizzer .
docker run --rm -p 8080:8080 quizzer
```

The Dockerfile uses Java 25 build/runtime images, exposes port 8080, and skips tests during image creation. Run the Maven tests separately before building a release image. Container data uses the same in-memory H2 defaults unless another profile is activated.

`application-rahti.properties` defines a PostgreSQL configuration for the `rahti` profile:

| Environment variable | Purpose |
|---|---|
| `SPRING_PROFILES_ACTIVE` | Set to `rahti` to activate this configuration |
| `DB_SERVICE_HOST` | PostgreSQL host |
| `DB_SERVICE_PORT` | PostgreSQL port |
| `DB_NAME` | Database name |
| `DB_USER` | Database username |
| `DB_PASSWORD` | Database password |

**Deployment preparation remains incomplete:** `pom.xml` does not yet include the PostgreSQL JDBC driver required by this profile. The profile also inherits the enabled H2 console setting from the default configuration. Review these settings before production deployment. Hibernate is configured with `ddl-auto=update`. No production deployment URL is recorded here yet.

## Team and project documentation

- [Samir B K](https://github.com/Samir-BK)
- [Prastab Parajuli](https://github.com/Prastab-Parajuli)
- [Toluwalase Ayoola](https://github.com/A2p3kt)
- [Sakibul Islam](https://github.com/sakibul2024)

[Team backlog and sprint board](https://github.com/orgs/Team-DevElite/projects/1)

- [AGENT.md](./AGENT.md): contributor workflow and architectural guidelines.
- [PROJECT_SPEC.md](./PROJECT_SPEC.md): product backlog and intended design.
- [PLAN.md](./PLAN.md): sprint tasks and implementation roadmap.
