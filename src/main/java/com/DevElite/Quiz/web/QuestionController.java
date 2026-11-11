package com.DevElite.Quiz.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.domain.QuestionRepository;
import com.DevElite.Quiz.service.QuizService;

/**
 * Controller for the teacher's question pages (User Story 6).
 */
@Controller
public class QuestionController {

    private final QuestionRepository questionRepository;
    private final QuizService quizService;

    public QuestionController(QuestionRepository questionRepository, QuizService quizService) {
        this.questionRepository = questionRepository;
        this.quizService = quizService;
    }

    /**
     * Shows all questions of one quiz so the teacher knows which questions it has.
     *
     * @param quizId ID of the quiz
     * @param model  passes the quiz and its questions to the view
     * @return the "questions" view; 404 if the quiz does not exist
     */
    @GetMapping("/quizzes/{quizId}/questions")
    public String listQuestions(@PathVariable("quizId") Long quizId, Model model) {
        Quiz quiz = quizService.getQuizById(quizId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found: " + quizId));
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questionRepository.findByQuizId(quizId));
        return "questions";
    }
}