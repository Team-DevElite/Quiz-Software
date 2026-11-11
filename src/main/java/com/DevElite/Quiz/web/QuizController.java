package com.DevElite.Quiz.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.service.QuizService;

/**
 * Controller handling teacher web interface interactions for Quiz management.
 * 
 * <p>Serves server-rendered Thymeleaf templates for listing, adding, editing,
 * and deleting quizzes (User Stories 1 through 4).</p>
 */
@Controller
public class QuizController {

    private final QuizService quizService;

    /**
     * Constructor injection for QuizService dependency.
     * 
     * @param quizService Service layer handling quiz business logic and persistence
     */
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    /**
     * Displays the front page listing all created quizzes (User Story 2).
     * 
     * <p>Mapped to both the application root ("/") and "/quizzes". Fetches all quizzes
     * from the database and binds them to the model under the attribute name "quizzes".</p>
     * 
     * @param model Spring UI model used to pass data to the Thymeleaf view
     * @return The "quizzes" Thymeleaf view template name
     */
    @GetMapping({"/", "/quizzes"})
    public String listQuizzes(Model model) {
        model.addAttribute("quizzes", quizService.getAllQuizzes());
        return "quizzes";
    }

    /**
     * Renders the form for creating a new quiz (User Story 1).
     * 
     * @param model Spring UI model used to bind an empty Quiz object
     * @return The "add-quiz" Thymeleaf view template name
     */
    @GetMapping("/quizzes/add")
    public String showAddQuizForm(Model model) {
        model.addAttribute("quiz", new Quiz());
        return "add-quiz";
    }

    /**
     * Handles submission of the quiz creation form (User Story 1).
     * 
     * @param quiz The quiz object bound from the submitted form fields
     * @return Redirect URL back to the quiz list ("/quizzes")
     */
    @PostMapping("/quizzes/add")
    public String saveQuiz(@ModelAttribute("quiz") Quiz quiz) {
        quizService.saveQuiz(quiz);
        return "redirect:/quizzes";
    }

    /**
     * Renders the form for editing an existing quiz (User Story 3).
     * 
     * @param id The ID of the quiz to edit
     * @param model Spring UI model used to bind the existing Quiz data
     * @return The "edit-quiz" Thymeleaf view template name
     */
    @GetMapping("/quizzes/edit/{id}")
    public String showEditQuizForm(@PathVariable("id") Long id, Model model) {
        Quiz quiz = quizService.getQuizById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid quiz ID: " + id));
        model.addAttribute("quiz", quiz);
        return "edit-quiz";
    }

    /**
     * Handles submission of the quiz edit form (User Story 3).
     * 
     * @param id The ID of the quiz to update
     * @param quiz The quiz form data with updated values
     * @return Redirect URL back to the quiz list ("/quizzes")
     */
    @PostMapping("/quizzes/edit/{id}")
    public String updateQuiz(@PathVariable("id") Long id, @ModelAttribute("quiz") Quiz quiz) {
        quizService.updateQuiz(id, quiz);
        return "redirect:/quizzes";
    }

    /**
     * Deletes a quiz by its primary key ID (User Story 4).
     * 
     * <p>Cascades deletion down to all child questions and answer options.</p>
     * 
     * @param id The ID of the quiz to delete
     * @return Redirect URL back to the quiz list ("/quizzes")
     */
    @GetMapping("/quizzes/delete/{id}")
    public String deleteQuiz(@PathVariable("id") Long id) {
        quizService.deleteQuiz(id);
        return "redirect:/quizzes";
    }
}
