package com.DevElite.Quiz.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import com.DevElite.Quiz.domain.Question;
import com.DevElite.Quiz.domain.QuestionRepository;
import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.service.QuizService;

/**
 * Controller for the teacher's question pages (User Stories 5, 6, and 7).
 * 
 * <p>
 * Handles listing questions for a quiz, presenting and processing the
 * add-question
 * form, and cascading deletion of a question along with its associated answer
 * options.
 * </p>
 */
@Controller
public class QuestionController {

    private final QuestionRepository questionRepository;
    private final QuizService quizService;

    /**
     * Constructor injection for required dependencies.
     * 
     * @param questionRepository Spring Data repository for Question entities
     * @param quizService        Service layer for Quiz management
     */
    public QuestionController(QuestionRepository questionRepository, QuizService quizService) {
        this.questionRepository = questionRepository;
        this.quizService = quizService;
    }

    /**
     * Shows all questions of one quiz so the teacher knows which questions it has
     * (User Story 6).
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

    /**
     * Renders the form for adding a new question to a quiz (User Story 5).
     * 
     * @param quizId ID of the parent quiz
     * @param model  Spring UI model used to pass the quiz and empty Question object
     * @return the "add-question" Thymeleaf template name
     */
    @GetMapping("/quizzes/{quizId}/questions/add")
    public String showAddQuestionForm(@PathVariable("quizId") Long quizId, Model model) {
        Quiz quiz = quizService.getQuizById(quizId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found: " + quizId));
        Question question = new Question();
        question.setDifficulty("Normal");
        model.addAttribute("quiz", quiz);
        model.addAttribute("question", question);
        return "add-question";
    }

    /**
     * Handles submission of the new question form (User Story 5).
     * 
     * @param quizId   ID of the parent quiz
     * @param question The question object bound from the form
     * @return Redirect URL back to the quiz's question list
     *         ("/quizzes/{quizId}/questions")
     */
    @PostMapping("/quizzes/{quizId}/questions/add")
    public String saveQuestion(@PathVariable("quizId") Long quizId,
            @ModelAttribute("question") Question question) {
        Quiz quiz = quizService.getQuizById(quizId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found: " + quizId));
        question.setQuiz(quiz);
        if (question.getDifficulty() == null || question.getDifficulty().isBlank()) {
            question.setDifficulty("Normal");
        }
        questionRepository.save(question);
        return "redirect:/quizzes/" + quizId + "/questions";
    }

    /**
     * Deletes a question and its child answer options, then redirects back to the
     * quiz question list (User Story 7).
     * 
     * @param id The ID of the question to delete
     * @return Redirect URL back to the parent quiz's questions page
     */
    @GetMapping("/questions/delete/{id}")
    public String deleteQuestion(@PathVariable("id") Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found: " + id));
        Long quizId = question.getQuiz().getId();
        questionRepository.delete(question);
        return "redirect:/quizzes/" + quizId + "/questions";
    }
}
