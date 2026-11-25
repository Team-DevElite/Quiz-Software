package com.DevElite.Quiz.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.DevElite.Quiz.domain.Category;
import com.DevElite.Quiz.domain.CategoryRepository;
import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.domain.QuizRepository;

/**
 * Student-facing JSON endpoints for categories at /api/categories
 * (issue #45, user story #25: students browse the list of added categories).
 *
 * <p>Reads go straight through {@link CategoryRepository}; the teacher-side
 * CategoryService (add/list/delete with quiz detachment) is separate work.
 * Category.quizzes is @JsonIgnore'd, so responses are flat
 * {@code {id, name, description}} objects.</p>
 */
// Allow the React/Vite student dashboard (different origin) to call these endpoints.
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {

    private final CategoryRepository categoryRepository;
    private final QuizRepository quizRepository;

    /**
     * @param categoryRepository persistence for categories
     * @param quizRepository persistence for quizzes (used for the quizzes-of-category list)
     */
    public CategoryRestController(CategoryRepository categoryRepository, QuizRepository quizRepository) {
        this.categoryRepository = categoryRepository;
        this.quizRepository = quizRepository;
    }

    /**
     * Lists all categories.
     * @return every category, or an empty list when none have been added
     */
    @GetMapping
    public Iterable<Category> getCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Returns a single category, e.g. for the header of a category's quiz page.
     * @param id category identifier
     * @return the category
     * @throws ResponseStatusException 404 Not Found if no category has this id
     */
    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Category with id " + id + " not found"));
    }

    /**
     * Lists the published quizzes of a category (user story #26).
     * @param id category identifier
     * @return published quizzes in the category, or an empty list when there are none
     * @throws ResponseStatusException 404 Not Found if no category has this id
     */
    @GetMapping("/{id}/quizzes")
    public Iterable<Quiz> getQuizzesOfCategory(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Category with id " + id + " not found");
        }
        return quizRepository.findByCategoryIdAndIsPublishedTrue(id);
    }
}