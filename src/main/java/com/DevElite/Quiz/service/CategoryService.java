package com.DevElite.Quiz.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.DevElite.Quiz.domain.Category;
import com.DevElite.Quiz.domain.CategoryRepository;
import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.domain.QuizRepository;

/** Business logic and persistence boundaries for teacher category management. */
@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final QuizRepository quizRepository;

    /**
     * @param categoryRepository repository used to persist categories
     * @param quizRepository repository used to detach quizzes before category deletion
     */
    public CategoryService(CategoryRepository categoryRepository, QuizRepository quizRepository) {
        this.categoryRepository = categoryRepository;
        this.quizRepository = quizRepository;
    }

    /** @return all categories available for browsing and assignment */
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /** @param id category identifier @return category if present */
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    /**
     * Saves a category after checking database column limits and its required name.
     * @param category category to persist
     * @return persisted category
     * @throws IllegalArgumentException for missing names or fields exceeding 255 characters
     */
    @Transactional
    public Category saveCategory(Category category) {
        if (category.getName() == null || category.getName().isBlank()) {
            throw new IllegalArgumentException("Category name is required.");
        }
        if (category.getName().length() > 255 ||
                (category.getDescription() != null && category.getDescription().length() > 255)) {
            throw new IllegalArgumentException("Category fields must not exceed 255 characters.");
        }
        category.setName(category.getName().strip());
        return categoryRepository.save(category);
    }

    /**
     * Detaches all quizzes referencing the category (setting {@code category} to {@code null})
     * and deletes the category inside one transaction so quizzes are preserved without
     * foreign-key constraint violations (S2-US4 / Exercise 8).
     *
     * @param id category identifier
     */
    @Transactional
    public void deleteCategory(Long id) {
        List<Quiz> assignedQuizzes = quizRepository.findByCategoryId(id);
        for (Quiz quiz : assignedQuizzes) {
            quiz.setCategory(null);
        }
        quizRepository.saveAll(assignedQuizzes);

        categoryRepository.findById(id).ifPresent(category -> {
            category.getQuizzes().clear();
            categoryRepository.delete(category);
        });
    }
}
