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

## Sprint 1 Overview (Completed)
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
- [x] **Task 7.4**: Fix frontend CI `react-hooks/set-state-in-effect` failure: reset loading/error in refresh button handlers, fetch asynchronously in the effect, and abort stale requests during cleanup. Verified with `npm run lint` and `npm run build`.

---

## Milestone 8: Documentation, Verification, Seed Data & Release (Exercises 22, 23, 24, 25)
- [x] **Task 8.1**: Create database seeder (`CommandLineRunner`) with realistic demo data (Scrum framework, Java fundamentals quizzes with questions and answers) with empty repository check. *(Completed in `QuizApplication.java`)*
- [x] **Task 8.2**: Verify and refine Developer Guide in `README.md` (exact command line instructions, port information, H2 credentials). Updated to reflect current routes, feature status, build version, and container/Rahti configuration. Documentation checked against source; `git diff --check` passed.
- [x] **Task 8.4**: Prepare GitHub Release notes (`sprint1` tag) and Sprint Review presentation demo guide. *(Completed in `RELEASE_NOTES.md`)*

---

## Sprint 2 Overview
- **Deadline**: Tuesday 1.12. at 23:59
- **Goal**: Deliver Teacher Category Management, full Student REST API with DTO validation and Swagger/OpenAPI documentation, multi-page Student Dashboard (quiz taking with feedback, results summary, category browsing), updated documentation (`Data model`, `REST API`, `Developer guide`, `Retrospectives`), and Sprint 2 production deployment & release (`sprint2`).

---

## Milestone 9: Sprint 2 Setup, Retrospective & Backend Prerequisites (Ex 1–4, 9)
- [ ] **Task 9.1** *(Ex 1–2)*: Conduct Sprint 1 Mad/Sad/Glad Retrospective on Flinga, rotate Scrum Master, and add `## Retrospectives` with the Flinga link to `README.md`.
- [ ] **Task 9.2** *(Ex 3–4)*: Close Sprint 1 GitHub issues/stories, create the `Sprint 2` milestone and `user story` / `backend` / `frontend` labels, and create Sprint 2 backlog issues.
- [ ] **Task 9.3**: Add `spring-boot-starter-validation` to `pom.xml` and set `server.error.include-message=always` in `application.properties` and `application-rahti.properties`.
- [ ] **Task 9.4** *(Ex 9)*: Add `## Data model` (Mermaid `erDiagram` + entity/attribute/relationship prose) to `README.md` and keep it updated as new entities are added.

---

## Milestone 10: Teacher Dashboard — Category Management (S2-US1, S2-US2, S2-US3, S2-US4 / Ex 5–8)
- [x] **Task 10.1a** *(Ex 5, user-requested entity/repository scope)*: Create `Category` JPA entity (`id`, `name`, `description`, `@JsonIgnore @OneToMany quizzes`) and `CategoryRepository`, including the nullable owning `Quiz.category` mapping required by `mappedBy`. Add repository and serialization verification.
- [ ] **Task 10.1b** *(Ex 5)*: Create `CategoryService`.
- [ ] **Task 10.2** *(Ex 5)*: Create `CategoryController` with `GET /categories/add` and `POST /categories/add`, `add-category.html`, update `QuizApplication.java` seeder with categories, and add integration tests.
- [ ] **Task 10.3** *(Ex 6)*: Implement `GET /categories` in `CategoryController`, create `categories.html`, add Quizzes/Categories navigation links in `quizzes.html` and `categories.html`, and add `MockMvc` tests.
- [ ] **Task 10.4** *(Ex 7)*: Nullable `@ManyToOne Category category` mapping completed in Task 10.1a; update `QuizService.updateQuiz()` to persist category changes, expose `categories` in `QuizController` add/edit forms, add `<select>` dropdown in `add-quiz.html` and `edit-quiz.html`, show Category column in `quizzes.html`, link seeded quizzes to categories, and add integration tests.
- [ ] **Task 10.5** *(Ex 8)*: Add `findByCategoryId` to `QuizRepository`, implement `@Transactional deleteCategory` detaching quizzes (`quiz.setCategory(null)`) before deletion, add `GET /categories/delete/{id}` in `CategoryController`, add Delete button with confirmation in `categories.html`, and add tests verifying quizzes survive category deletion.

---

## Milestone 11: Student REST API — Quizzes, Questions, Answers & Results (Ex 10–13)
- [ ] **Task 11.1** *(Ex 10)*: Implement `GET /api/quizzes/{id}` in `QuizRestController` returning `404 Not Found` (`ResponseStatusException`) when quiz ID does not exist + `MockMvc` tests.
- [ ] **Task 11.2** *(Ex 11)*: Implement `GET /api/quizzes/{id}/questions` returning questions with nested `answerOptions` (using `@JsonManagedReference`/`@JsonBackReference` or DTOs) sorted `Easy` → `Normal` → `Hard`, returning `404 Not Found` if quiz does not exist + `MockMvc` tests.
- [ ] **Task 11.3** *(Ex 12)*: Create `Answer` JPA entity (`@ManyToOne AnswerOption` with `@OnDelete(CASCADE)`), `AnswerRepository`, `CreateAnswerDto` (`@NotNull answerOptionId`), `AnswerResponseDto`, and `AnswerRestController` (`POST /api/answers`) handling null `answerOptionId` (`400`), nonexistent `answerOptionId` (`404`), and unpublished quiz (`400`/`403`) + tests.
- [ ] **Task 11.4** *(Ex 13)*: Create `QuestionResultDto` (`questionId`, `questionText`, `questionDifficulty`, `totalAnswers`, `correctAnswers`, `wrongAnswers`), add aggregation logic in service/repository (including 0-answer questions), and implement `GET /api/quizzes/{id}/results` (`404 Not Found` if quiz missing) + `MockMvc` tests.

---

## Milestone 12: Student REST API — Categories & Swagger Documentation (Ex 14–17)
- [x] **Task 12.1** *(Ex 14–15, issue #45)*: Create `CategoryRestController` (`@RequestMapping("/api/categories")`, `@CrossOrigin(origins = "*")`) with `GET /api/categories` and `GET /api/categories/{id}` (`404 Not Found` if missing) + `MockMvc` tests.
- [ ] **Task 12.2** *(Ex 16)*: Add `findByCategoryIdAndIsPublishedTrue(Long categoryId)` to `QuizRepository` and implement `GET /api/categories/{id}/quizzes` returning only published quizzes of the category (`404 Not Found` if category missing) + `MockMvc` tests.
- [ ] **Task 12.3** *(Ex 17)*: Add `org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.15` to `pom.xml`, annotate all REST controllers/methods with `@Tag`, `@Operation`, and `@ApiResponses`/`@ApiResponse`, test all endpoints & error cases in `/swagger-ui/index.html`, and add the Rahti Swagger link under `## REST API` in `README.md`.

---

## Milestone 13: Student Frontend — Navigation, Quizzes, Answering & Results (S2-US5, S2-US6, S2-US7, S2-US8 / Ex 18–21)
- [ ] **Task 13.1** *(Ex 18)*: Install `react-router-dom`, extract API service modules (`frontend/src/services/quizService.ts`, `categoryService.ts`), add top navigation menu (`Quizzes`, `Categories`) in `App.tsx`, update `types.ts` and `QuizCard.tsx` to display `category`, turn quiz name into a link to `/quizzes/:id` (removing placeholder `alert()`), and add a Results link (`/quizzes/:id/results`) next to each quiz.
  - [x] *Partial (issues #38/#39, story #21)*: `GET /api/quizzes` category serialization verified by `QuizRestCategoryTests`; `Category` type added to `types.ts` and `QuizCard.tsx` shows a category badge (or "Uncategorized").
- [ ] **Task 13.2** *(Ex 19)*: Create `QuizPage.tsx` (`/quizzes/:id`) showing quiz name, description, and questions ordered `Easy` → `Normal` → `Hard` with their answer options, loading/error/404/empty states, and back link.
- [ ] **Task 13.3** *(Ex 20)*: Add per-question radio selection and Submit button in `QuizPage.tsx` calling `POST /api/answers`, showing immediate feedback dialog (*"That is correct, good job!"* / *"That is not correct, try again"*).
- [ ] **Task 13.4** *(Ex 21)*: Create `QuizResultsPage.tsx` (`/quizzes/:id/results`) displaying quiz total questions and total answers, and per-question text, difficulty, total answers, correct percentage (guarding `0` answers), correct count, and wrong count.

---

## Milestone 14: Student Frontend — Category Browsing (S2-US9, S2-US10 / Ex 22–23)
- [ ] **Task 14.1** *(Ex 22)*: Create `CategoryListPage.tsx` (`/categories`) listing all categories with descriptions and clickable category names linking to `/categories/:id/quizzes`, with loading/error/empty states.
- [ ] **Task 14.2** *(Ex 23)*: Create `CategoryQuizzesPage.tsx` (`/categories/:id/quizzes`) displaying category name/description, back navigation, and published quizzes in that category reusing `QuizList`/`QuizCard` (preserving quiz-taking and results links).

---

## Milestone 15: Production Deployment, Developer Guide, Release & Sprint Review (Ex 24–27)
- [ ] **Task 15.1** *(Ex 24)*: Deploy Sprint 2 backend to Rahti and frontend to Render (with SPA rewrite `/* -> /index.html`), test answering quizzes & viewing results in production, and verify live URL in `README.md`.
- [ ] **Task 15.2** *(Ex 25)*: Restructure `README.md` `## Developer guide` into `### Backend` and `### Frontend` (exact directory, install/run commands, backend/frontend tech stacks, H2 dev vs. PostgreSQL prod DBs) and remove outdated PostgreSQL driver note.
- [ ] **Task 15.3** *(Ex 26–27)*: Push all work to `main`, create GitHub Release tag `sprint2` titled `"Sprint 2"`, verify realistic demo data, and prepare Sprint Review presentation before Tue 1.12. 23:59.
