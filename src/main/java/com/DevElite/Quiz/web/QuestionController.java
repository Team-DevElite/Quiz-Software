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

@RestController
@RequestMapping("/api/quizzes")
public class QuestionController {

    private final AddQuizService addQuizService;
    private final QuizRepository quizRepository;

    public QuestionController(AddQuizService addQuizService, QuizRepository quizRepository) {
        this.addQuizService = addQuizService;
        this.quizRepository = quizRepository;
    }

    @PostMapping
    public ResponseEntity<Quiz> addQuiz(@RequestBody Quiz quiz) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addQuizService.addQuiz(quiz));
    }

    @GetMapping
    public Iterable<Quiz> getQuizzes() {
        return quizRepository.findAll();
    }
}
