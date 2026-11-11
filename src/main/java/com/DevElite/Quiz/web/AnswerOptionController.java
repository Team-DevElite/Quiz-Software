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


@Controller
public class AnswerOptionController {

    private final AnswerOptionService answerOptionService;
    private final AnswerOptionRepository answerOptionRepository;

 
    public AnswerOptionController(AnswerOptionService answerOptionService, 
                                  AnswerOptionRepository answerOptionRepository) {
        this.answerOptionService = answerOptionService;
        this.answerOptionRepository = answerOptionRepository;
    }

   
    @GetMapping("/questions/{questionId}/options")
    public String listOptions(@PathVariable("questionId") Long questionId, Model model) {
        Question question = answerOptionService.getQuestionById(questionId);
        model.addAttribute("question", question);
        model.addAttribute("options", answerOptionService.getOptionsByQuestionId(questionId));
        return "options";
    }

   
    @GetMapping("/questions/{questionId}/options/add")
    public String showAddOptionForm(@PathVariable("questionId") Long questionId, Model model) {
        Question question = answerOptionService.getQuestionById(questionId);
        model.addAttribute("question", question);
        model.addAttribute("option", new AnswerOption());
        return "add-option";
    }

    @PostMapping("/questions/{questionId}/options/add")
    public String saveOption(@PathVariable("questionId") Long questionId,
                             @ModelAttribute("option") AnswerOption option) {
        answerOptionService.addOption(questionId, option);
        return "redirect:/questions/" + questionId + "/options";
    }

    @GetMapping("/options/delete/{id}")
    public String deleteOption(@PathVariable("id") Long id) {
        AnswerOption option = answerOptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Option not found with id: " + id));
        
        Long questionId = option.getQuestion().getId();
        
        answerOptionService.deleteOption(id);
        
        return "redirect:/questions/" + questionId + "/options";
    }
}