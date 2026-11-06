package com.DevElite.Quiz.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.domain.QuizRepository;
import com.DevElite.Quiz.service.AddQuizService;

/**
 * Controller currently managing quiz endpoints at /api/quizzes.
 * 
 * <p>Note: Per Milestone 6 in PLAN.md, this REST functionality will be cleanly
 * migrated to QuizRestController, allowing QuestionController to manage teacher
 * question views (/quizzes/{quizId}/questions).</p>
 */
@RestController
@RequestMapping("/api/quizzes")
public class QuestionController {

    private final AddQuizService addQuizService;
    private final QuizRepository quizRepository;

    /**
     * Constructor injection for required dependencies.
     */
    public QuestionController(AddQuizService addQuizService, QuizRepository quizRepository) {
        this.addQuizService = addQuizService;
        this.quizRepository = quizRepository;
    }

    /**
     * POST endpoint to create a new quiz via JSON payload.
     * 
     * @param quiz Quiz entity deserialized from request body
     * @return 201 CREATED status with the saved Quiz
     */
    @PostMapping
    public ResponseEntity<Quiz> addQuiz(@RequestBody Quiz quiz) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addQuizService.addQuiz(quiz));
    }

    /**
     * GET endpoint to retrieve all quizzes.
     * 
     * @return Iterable collection of all quizzes in database
     */
    @GetMapping
    public Iterable<Quiz> getQuizzes() {
        return quizRepository.findAll();
    }
}
