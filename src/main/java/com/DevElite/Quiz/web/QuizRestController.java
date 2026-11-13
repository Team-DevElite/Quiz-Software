package com.DevElite.Quiz.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.service.QuizService;

/**
 * REST endpoints for quizzes at /api/quizzes (moved from QuestionController).
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/quizzes")
public class QuizRestController {

    private final QuizService quizService;

    public QuizRestController(QuizService quizService) {
        this.quizService = quizService;
    }

    /*
    @PostMapping
    public ResponseEntity<Quiz> addQuiz(@RequestBody Quiz quiz) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizService.saveQuiz(quiz));
    }
    */

    @GetMapping
    public Iterable<Quiz> getQuizzes() {
        return quizService.getPublishedQuizzes();
    }
}
