package com.DevElite.Quiz.service;

import org.springframework.stereotype.Service;

import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.domain.QuizRepository;

/**
 * Service class handling quiz creation and persistence logic.
 */
@Service
public class AddQuizService {

    private final QuizRepository quizRepository;

    /**
     * Constructor injection for required QuizRepository dependency.
     * 
     * @param quizRepository Repository used for Quiz database operations
     */
    public AddQuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    /**
     * Persist a new quiz to the database.
     * 
     * @param quiz The Quiz entity to save
     * @return The persisted Quiz with generated ID
     */
    public Quiz addQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }
}
