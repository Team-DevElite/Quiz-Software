package com.DevElite.Quiz.web;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        quizRepository.delete(quiz.get());
        return ResponseEntity.noContent().build();
    }
}
