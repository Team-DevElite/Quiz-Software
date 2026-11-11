package com.DevElite.Quiz.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.DevElite.Quiz.domain.AnswerOption;
import com.DevElite.Quiz.domain.AnswerOptionRepository;
import com.DevElite.Quiz.domain.Question;
import com.DevElite.Quiz.service.AnswerOptionService;

/**
 * Controller handling teacher web interface interactions for Answer Option management (User Stories 8, 9, and 10).
 */
@Controller
public class AnswerOptionController {

    private final AnswerOptionService answerOptionService;
    private final AnswerOptionRepository answerOptionRepository;

    /**
     * Constructor injection for required dependencies.
     * 
     * @param answerOptionService Service layer for answer options
     * @param answerOptionRepository Repository for answer options
     */
    public AnswerOptionController(AnswerOptionService answerOptionService, 
                                  AnswerOptionRepository answerOptionRepository) {
        this.answerOptionService = answerOptionService;
        this.answerOptionRepository = answerOptionRepository;
    }

    /**
     * Displays all answer options belonging to a specific question (User Story 9).
     * 
     * @param questionId ID of the parent question
     * @param model Spring UI model used to pass the question and options
     * @return The "options" Thymeleaf view template name
     */
    @GetMapping("/questions/{questionId}/options")
    public String listOptions(@PathVariable("questionId") Long questionId, Model model) {
        Question question = answerOptionService.getQuestionById(questionId);
        model.addAttribute("question", question);
        model.addAttribute("options", answerOptionService.getOptionsByQuestionId(questionId));
        return "options";
    }

    /**
     * Renders the form to add a new answer option to a question (User Story 8).
     * 
     * @param questionId ID of the parent question
     * @param model Spring UI model used to pass the question and new AnswerOption
     * @return The "add-option" Thymeleaf view template name
     */
    @GetMapping("/questions/{questionId}/options/add")
    public String showAddOptionForm(@PathVariable("questionId") Long questionId, Model model) {
        Question question = answerOptionService.getQuestionById(questionId);
        model.addAttribute("question", question);
        model.addAttribute("option", new AnswerOption());
        return "add-option";
    }

    /**
     * Handles submission of the new answer option form (User Story 8).
     * 
     * @param questionId ID of the parent question
     * @param option The new AnswerOption bound from the form
     * @return Redirect URL back to the question's answer options list
     */
    @PostMapping("/questions/{questionId}/options/add")
    public String saveOption(@PathVariable("questionId") Long questionId,
                             @ModelAttribute("option") AnswerOption option) {
        answerOptionService.addOption(questionId, option);
        return "redirect:/questions/" + questionId + "/options";
    }

    /**
     * Renders the form for editing an existing answer option (User Story 9/10).
     * 
     * @param id The ID of the answer option to edit
     * @param model Spring UI model used to pass the existing option and parent question
     * @return The "edit-option" Thymeleaf view template name
     */
    @GetMapping("/options/edit/{id}")
    public String showEditOptionForm(@PathVariable("id") Long id, Model model) {
        AnswerOption option = answerOptionService.getOptionById(id);
        model.addAttribute("option", option);
        model.addAttribute("question", option.getQuestion());
        return "edit-option";
    }

    /**
     * Handles submission of the edited answer option form (User Story 9/10).
     * 
     * @param id The ID of the answer option being updated
     * @param option The updated AnswerOption bound from the form
     * @return Redirect URL back to the question's answer options list
     */
    @PostMapping("/options/edit/{id}")
    public String updateOption(@PathVariable("id") Long id,
                               @ModelAttribute("option") AnswerOption option) {
        AnswerOption existing = answerOptionService.getOptionById(id);
        Long questionId = existing.getQuestion().getId();
        answerOptionService.updateOption(id, option);
        return "redirect:/questions/" + questionId + "/options";
    }

    /**
     * Deletes an answer option and redirects back to the question's options list (User Story 10).
     * 
     * @param id The ID of the answer option to delete
     * @return Redirect URL back to the parent question's options list
     */
    @GetMapping("/options/delete/{id}")
    public String deleteOption(@PathVariable("id") Long id) {
        AnswerOption option = answerOptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Option not found with id: " + id));
        
        Long questionId = option.getQuestion().getId();
        answerOptionService.deleteOption(id);
        
        return "redirect:/questions/" + questionId + "/options";
    }
}
