package com.DevElite.Quiz.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.domain.QuizRepository;

/**
 * Service class encapsulating business logic and CRUD operations for {@link Quiz} entities.
 */
@Service
public class QuizService {

    private final QuizRepository quizRepository;

    /**
     * Constructor injection for QuizRepository.
     * 
     * @param quizRepository Repository used for database persistence
     */
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    /**
     * Retrieve all quizzes from the database.
     * 
     * @return Iterable collection of all quizzes
     */
    public Iterable<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    /**
     * Find a quiz by its primary key ID.
     * 
     * @param id The quiz ID
     * @return Optional containing the Quiz if found, or empty Optional
     */
    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    /**
     * Persist a new or modified quiz.
     * 
     * <p>Ensures that a creation timestamp is set if not already present.</p>
     * 
     * @param quiz The Quiz entity to save
     * @return The persisted Quiz with generated ID
     */
    public Quiz saveQuiz(Quiz quiz) {
        if (quiz.getCreatedAt() == null) {
            quiz.setCreatedAt(LocalDate.now());
        }
        return quizRepository.save(quiz);
    }

    /**
     * Backward-compatible alias for {@link #saveQuiz(Quiz)}.
     * 
     * @param quiz The Quiz entity to save
     * @return The persisted Quiz
     */
    public Quiz addQuiz(Quiz quiz) {
        return saveQuiz(quiz);
    }

    /**
     * Update an existing quiz's editable attributes (User Story 3).
     * 
     * @param id The ID of the quiz to update
     * @param updatedQuiz Form data containing updated name, description, courseCode, and isPublished
     * @return The updated Quiz entity
     * @throws IllegalArgumentException if no quiz exists with the given ID
     */
    public Quiz updateQuiz(Long id, Quiz updatedQuiz) {
        Quiz existingQuiz = quizRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + id));

        existingQuiz.setName(updatedQuiz.getName());
        existingQuiz.setDescription(updatedQuiz.getDescription());
        existingQuiz.setCourseCode(updatedQuiz.getCourseCode());
        existingQuiz.setIsPublished(updatedQuiz.getIsPublished());

        return quizRepository.save(existingQuiz);
    }

    /**
     * Delete a quiz by its ID (User Story 4).
     * 
     * <p>Cascades deletion down to all child questions and answer options.</p>
     * 
     * @param id The ID of the quiz to delete
     */
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    /**
     * Retrieve only quizzes with published status = true.
     * 
     * @return List of published quizzes for students
     */
    public List<Quiz> getPublishedQuizzes() {
        return quizRepository.findByIsPublishedTrue();
    }
}

