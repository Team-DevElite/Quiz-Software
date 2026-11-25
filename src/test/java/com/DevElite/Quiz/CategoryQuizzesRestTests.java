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

/** MockMvc tests for GET /api/categories/{id}/quizzes (user story #26). */
@SpringBootTest
@AutoConfigureMockMvc
class CategoryQuizzesRestTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuizRepository quizRepository;

    /** Only published quizzes of the requested category are returned. */
    @Test
    void returnsOnlyPublishedQuizzesOfCategory() throws Exception {
        Category category = categoryRepository.save(new Category("CQ Scrum", "Scrum topics"));
        Category other = categoryRepository.save(new Category("CQ Other", "Other topics"));

        Quiz published = new Quiz("CQ Published", "Visible", "CQ101", true);
        published.setCategory(category);
        quizRepository.save(published);

        Quiz draft = new Quiz("CQ Draft", "Hidden", "CQ102", false);
        draft.setCategory(category);
        quizRepository.save(draft);

        Quiz elsewhere = new Quiz("CQ Elsewhere", "Other category", "CQ103", true);
        elsewhere.setCategory(other);
        quizRepository.save(elsewhere);

        mockMvc.perform(get("/api/categories/{id}/quizzes", category.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("CQ Published"))
                .andExpect(jsonPath("$[0].category.name").value("CQ Scrum"));
    }

    /** A category without published quizzes yields an empty list, not an error. */
    @Test
    void emptyCategoryReturnsEmptyList() throws Exception {
        Category category = categoryRepository.save(new Category("CQ Empty", "No quizzes"));

        mockMvc.perform(get("/api/categories/{id}/quizzes", category.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    /** Unknown category ids yield 404 Not Found. */
    @Test
    void unknownCategoryReturns404() throws Exception {
        mockMvc.perform(get("/api/categories/{id}/quizzes", 999_999L))
                .andExpect(status().isNotFound());
    }
}