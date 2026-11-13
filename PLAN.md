# PLAN.md — Active Sprint Backlog & Execution Roadmap

## 🛑 Strict Working Agreement (The Approval Gate)
1. **Never write or modify code without checking `PLAN.md` first.**
2. **Execute exactly ONE task at a time.**
3. **After completing a task**:
   - Update this file, checking off the task (`[x]`).
   - Summarize the changes and verify compilation/tests.
   - **WAIT for explicit user approval before moving to the next task.**
4. **Keep markdown documentation in sync with all architectural decisions.**

---

## Sprint 1 Overview
- **Deadline**: Tuesday 17.11. at 23:59
- **Goal**: Deliver full Teacher Dashboard (CRUD for Quizzes, Questions, and Answer Options), Student Dashboard (React SPA + REST API), and prepare for Rahti deployment & Sprint Review.

---

## Milestone 1: Environment, Dependencies & Build Foundation
- [x] **Task 1.1**: Initialize Git repository and add `.gitignore` for Java/Maven/VS Code.
- [x] **Task 1.2**: Configure in-memory H2 database and H2 console in `application.properties`.
- [x] **Task 1.3**: Resolve `pom.xml` build configuration (Set Spring Boot parent to `3.4.2`, replace invalid `spring-boot-starter-webmvc` with `spring-boot-starter-web`, add standard `spring-boot-starter-test`).
- [x] **Task 1.4**: Verify build environment & Maven wrapper configuration (Synchronized `distributionUrl` in `maven-wrapper.properties` with Maven 3.9.16 and verified starter configuration).

---

## Milestone 2: Domain Model, Service & Entity Relationships
- [x] **Task 2.1**: Create baseline `Quiz` JPA entity and `QuizRepository`.
- [x] **Task 2.2**: Implement `AddQuizService` (`service/AddQuizService.java`) and baseline test in `QuizApplicationTests.java`. *(Completed in commit `8924868` & `549bc90`)*
- [x] **Task 2.3**: Enhance `Quiz` entity with `createdAt` timestamp, proper camelCase `courseCode`, default `isPublished = false`, and bidirectional relationship to `Question`.
- [x] **Task 2.4**: Create `Question` JPA entity with fields (`id`, `questionText`, `difficulty` defaulting to `"Normal"`), `@ManyToOne` relationship to `Quiz`, and bidirectional relationship to `AnswerOption`.
- [x] **Task 2.5**: Create `AnswerOption` JPA entity with fields (`id`, `optionText`, `isCorrect` defaulting to `false`), and `@ManyToOne` relationship to `Question`.
- [x] **Task 2.6**: Configure `@JsonIgnore` on `@OneToMany` collections (`Quiz.questions`, `Question.answerOptions`) and establish `CascadeType.ALL` with `orphanRemoval = true`.
- [x] **Task 2.7**: Create `QuestionRepository` and `AnswerOptionRepository` interfaces and add query methods (e.g., `findByIsPublishedTrue()`, `findByQuizId()`, `findByQuestionId()`).

---

## Milestone 3: Teacher Dashboard — Quiz Management (US1, US2, US3, US4)
> *Note: Teacher dashboard uses server-rendered Thymeleaf HTML templates (separate from the `/api` REST endpoints).*
- [x] **Task 3.1**: Create `QuizController` (`web/QuizController.java`) and implement `GET /` and `GET /quizzes` to list all quizzes with `createdAt` date and action links (`quizzes.html`).
- [x] **Task 3.2**: Implement `GET /quizzes/add` and `POST /quizzes/add` with form validation, setting `createdAt = LocalDate.now()`, delegating to `QuizService`, and saving to database (`add-quiz.html`).
- [x] **Task 3.3**: Refactor `AddQuizService` to `QuizService` with full CRUD methods, and implement `GET /quizzes/edit/{id}` and `POST /quizzes/edit/{id}` (`edit-quiz.html`).
- [x] **Task 3.4**: Implement `GET /quizzes/delete/{id}` in `QuizController` to cascade delete a quiz and redirect to `/quizzes`.
- [x] **Task 3.5**: Add styling, responsive tables, badge indicators, and navigation breadcrumbs across all quiz templates.

---

## Milestone 4: Teacher Dashboard — Question Management (US5, US6, US7)
> *Note: Free up `QuestionController.java` to serve HTML views for question management.*
- [x] **Task 4.1**: Implement `GET /quizzes/{quizId}/questions` in `QuestionController` to display all questions for a specific quiz (`questions.html`). *(Completed in commit `446de28` & `3480649`)*
- [x] **Task 4.2**: Implement `GET /quizzes/{quizId}/questions/add` and `POST /quizzes/{quizId}/questions/add` to create and associate questions to a quiz (`add-question.html`).
- [x] **Task 4.3**: Implement `GET /questions/delete/{id}` in `QuestionController` to cascade delete a question and its answer options, redirecting to the quiz questions list.

---

## Milestone 5: Teacher Dashboard — Answer Option Management (US8, US9, US10)
- [x] **Task 5.1**: Create `AnswerOptionController` and implement `GET /questions/{questionId}/options` to display answer options with correctness indicators (`options.html`). *(Completed in commit `85a62d3`)*
- [x] **Task 5.2**: Implement `GET /questions/{questionId}/options/add` and `POST /questions/{questionId}/options/add` to add options to a question (`add-option.html`). *(Completed in commit `85a62d3`)*
- [x] **Task 5.3**: Implement `GET /options/edit/{id}` and `POST /options/edit/{id}` to update option text and correctness status (`edit-option.html`).
- [x] **Task 5.4**: Implement `GET /options/delete/{id}` to delete an answer option and return to the options view. *(Completed in commit `85a62d3`)*

---

## Milestone 6: Student Dashboard REST API & CORS (Exercise 20)
- [x] **Task 6.1**: Realign REST endpoints: Create `QuizRestController` under `com.DevElite.Quiz.web` (migrating the existing `/api/quizzes` endpoints out of `QuestionController`). *(Completed in commit `446de28` & `3480649`)*
- [x] **Task 6.2**: Update `GET /api/quizzes` to return only published quizzes (`quizRepository.findByIsPublishedTrue()`).
- [x] **Task 6.3**: Add `@CrossOrigin(origins = "*")` to `QuizRestController` to permit cross-origin requests from the student frontend.
- [x] **Task 6.4**: Write integration tests using `MockMvc` to verify only published quizzes are returned and JSON serialization does not cycle.

---

## Milestone 7: Student Dashboard Frontend Application (Exercise 19 & 21)
- [x] **Task 7.1**: Initialize a Vite + React student frontend project. *(Completed in `frontend/` directory)*
- [x] **Task 7.2**: Implement published quiz listing view in React fetching data from `http://localhost:8080/api/quizzes`.
- [x] **Task 7.3**: Add error handling, loading states, and basic styling.

---

## Milestone 8: Documentation, Verification, Seed Data & Release (Exercises 22, 23, 24, 25)
- [ ] **Task 8.1**: Create database seeder (`CommandLineRunner`) with realistic demo data (Scrum framework, Java fundamentals quizzes with questions and answers).
- [x] **Task 8.2**: Verify and refine Developer Guide in `README.md` (exact command line instructions, port information, H2 credentials). Updated to reflect current routes, feature status, build version, and container/Rahti configuration. Documentation checked against source; `git diff --check` passed. `./mvnw test` attempted but blocked by Mockito MockMaker initialization on the local Java 26 runtime.
- [ ] **Task 8.3**: Document Rahti production deployment steps (PostgreSQL setup, application properties, container configuration).
- [ ] **Task 8.4**: Prepare GitHub Release notes (`sprint1` tag) and Sprint Review presentation demo guide.
