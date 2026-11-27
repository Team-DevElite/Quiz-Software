package com.DevElite.Quiz.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import com.DevElite.Quiz.domain.Category;
import com.DevElite.Quiz.service.CategoryService;

/** Teacher MVC pages for creating, listing, and deleting quiz categories (Ex 5–8). */
@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    /** @param categoryService category business logic */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /** Restricts creation forms to editable category details. @param binder form binder */
    @InitBinder("category")
    public void configureBinding(WebDataBinder binder) {
        binder.setAllowedFields("name", "description");
    }

    /** @param model view model @return category list template */
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories";
    }

    /** @param model view model @return empty category creation form */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "add-category";
    }

    /**
     * Validates form details and redirects after saving to avoid duplicate submissions.
     * @param category submitted category details
     * @param errors binding and validation errors displayed in the form
     * @return form on invalid input, otherwise redirect to category list
     */
    @PostMapping("/add")
    public String saveCategory(@ModelAttribute("category") Category category, BindingResult errors) {
        if (category.getName() == null || category.getName().isBlank()) {
            errors.rejectValue("name", "required", "Category name is required.");
        } else if (category.getName().length() > 255) {
            errors.rejectValue("name", "length", "Name must not exceed 255 characters.");
        }
        if (category.getDescription() != null && category.getDescription().length() > 255) {
            errors.rejectValue("description", "length", "Description must not exceed 255 characters.");
        }
        if (errors.hasErrors()) return "add-category";
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }

    /**
     * Detaches any quizzes assigned to the category, deletes the category,
     * and redirects back to the category list (S2-US4 / Exercise 8).
     *
     * @param id category identifier
     * @return redirect to category list
     */
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
}
