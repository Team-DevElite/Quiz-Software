package com.DevElite.Quiz;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.DevElite.Quiz.domain.Category;
import com.DevElite.Quiz.domain.CategoryRepository;
import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.domain.QuizRepository;

/**
 * Verifies that the student quiz list (GET /api/quizzes) exposes each quiz's
 * category so the student dashboard can display it (issue #38, user story #21).
 */
@SpringBootTest
@AutoConfigureMockMvc
class QuizRestCategoryTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * A categorized quiz is serialized with a flat category object
     * (id, name, description, no inverse quizzes list), and an uncategorized
     * quiz is serialized with category = null.
     */
    @Test
    void publishedQuizzesIncludeCategoryInfo() throws Exception {
        Category category = categoryRepository.save(new Category("REST Category Test", "Category JSON check"));

        Quiz categorized = new Quiz("Categorized REST Quiz", "Has a category", "CAT101", true);
        categorized.setCategory(category);
        quizRepository.save(categorized);

        quizRepository.save(new Quiz("Uncategorized REST Quiz", "No category", "CAT102", true));

        mockMvc.perform(get("/api/quizzes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.name == 'Categorized REST Quiz')].category.id")
                        .value(category.getId().intValue()))
                .andExpect(jsonPath("$[?(@.name == 'Categorized REST Quiz')].category.name")
                        .value("REST Category Test"))
                .andExpect(jsonPath("$[?(@.name == 'Categorized REST Quiz')].category.description")
                        .value("Category JSON check"))
                // Category.quizzes is @JsonIgnore'd, so the payload cannot recurse.
                .andExpect(jsonPath("$[?(@.name == 'Categorized REST Quiz')].category.quizzes").doesNotExist())
                // Uncategorized quizzes still serialize the field, as null.
                .andExpect(jsonPath("$[?(@.name == 'Uncategorized REST Quiz')].category").value(org.hamcrest.Matchers.contains((Object) null)));
    }
}
