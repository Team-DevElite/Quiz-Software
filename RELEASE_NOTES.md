# Release Notes — Sprint 1 (`sprint1`)

**Release Version:** `v1.0.0-sprint1`  
**Team:** DevElite (Samir B K, Prastab Parajuli, Toluwalase Ayoola, Sakibul Islam)  
**Course:** Software Development Project (SOF005AS3AE)  
**Date:** November 2026  

---

## 🚀 Live Deployments

- **Teacher Dashboard (CSC Rahti Cloud):** [https://quiz-software-soft-dev-proj.2.rahtiapp.fi/](https://quiz-software-soft-dev-proj.2.rahtiapp.fi/)
- **Student Dashboard (Render):** [https://quizzer-653j.onrender.com/](https://quizzer-653j.onrender.com/)

---

## 📦 What's New in Sprint 1

### 1. Teacher Dashboard (Server-Side Thymeleaf MVC)
- **Quiz Management (US1, US2, US3, US4)**:
  - Full CRUD operations: Create new quizzes with validation, list all quizzes with metadata badges, edit existing quiz information and publication status, and delete quizzes.
  - Creation date (`createdAt`) automatically captured on creation.
- **Question Management (US5, US6, US7)**:
  - View all questions attached to a quiz.
  - Add new questions specifying prompt text and difficulty level (`Easy`, `Normal`, `Hard`).
  - Delete questions with database cascade to child options.
- **Answer Option Management (US8, US9, US10)**:
  - View all multiple-choice options for a question with correctness indicators.
  - Add new answer choices with correctness flags (`true`/`false`).
  - Edit existing answer option text and correctness status.
  - Delete answer options.

### 2. Student Dashboard & REST API (US11, Exercises 19–21)
- **REST API (`/api/quizzes`)**:
  - Exposes `GET /api/quizzes` returning **only published quizzes** (`isPublished == true`).
  - Cross-Origin Resource Sharing (CORS) enabled via `@CrossOrigin(origins = "*")`.
  - Infinite recursion prevention via `@JsonIgnore` on bidirectional relations.
- **Student SPA Frontend (React + TypeScript + Vite)**:
  - Standalone single-page application located in `frontend/`.
  - Real-time client-side search/filter by title, course code, or description.
  - Responsive card layout with dark/light mode support.
  - Comprehensive state handling: loading spinner, network error handling with retry button, and empty state display.
  - Configurable backend target via `.env` (`VITE_BACKEND_URL`).

### 3. Database Seeding & Data Persistence
- **Automatic Seed Data (`CommandLineRunner`)**:
  - Seeds realistic demo quizzes on startup if the database is empty:
    1. *The Scrum Framework* (Published, SOF005AS3AE, 2 questions with multiple options)
    2. *Java Fundamentals* (Published, CS101, 2 questions with multiple options)
    3. *Cloud & DevOps Architecture* (Draft / Unpublished, DO201)
  - Safe guard: Checks `quizRepository.count() == 0` before executing to prevent duplicate records or conflicts.

---

## 🎬 Sprint Review Presentation & Demo Guide

Follow this 5-minute presentation script during the Sprint Review demo:

### Step 1: Teacher Dashboard Walkthrough
1. Navigate to the teacher dashboard at `http://localhost:8080/` (or live on Rahti).
2. **Show Quizzes**: Point out seeded quizzes (*The Scrum Framework*, *Java Fundamentals*, and draft *Cloud & DevOps Architecture*).
3. **Show Questions & Options**:
   - Click "Questions" on *The Scrum Framework*.
   - Click "Options" on Question 1 to display the answer choices with correctness badges.
4. **Draft/Publish Feature**:
   - Note that *Cloud & DevOps Architecture* is marked as **Draft** (Unpublished).

### Step 2: REST API Verification
1. Open terminal or browser to `http://localhost:8080/api/quizzes`.
2. Observe the JSON array:
   - Contains *The Scrum Framework* and *Java Fundamentals*.
   - **Crucial**: Notice that the draft quiz *Cloud & DevOps Architecture* is **excluded**.

### Step 3: Student Dashboard Walkthrough
1. Open the student portal at `http://localhost:5173/` (or live at [https://quizzer-653j.onrender.com/](https://quizzer-653j.onrender.com/)).
2. Show the published quiz cards rendered in the modern UI.
3. Test the **Search Bar**: Type "Java" to filter dynamically to *Java Fundamentals*.
4. Test the **Teacher Portal link** and the **Refresh** button.

---

## 👥 Team DevElite Contributors
- Samir B K
- Prastab Parajuli
- Toluwalase Ayoola
- Sakibul Islam

