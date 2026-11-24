package com.DevElite.Quiz;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.DevElite.Quiz.domain.Category;
import com.DevElite.Quiz.domain.CategoryRepository;
import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.domain.QuizRepository;

/**
 * MockMvc tests for the student category REST API (issue #45, user story #25):
 * GET /api/categories and GET /api/categories/{id}.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CategoryRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuizRepository quizRepository;

    /** Lists categories as flat JSON with CORS enabled for the student dashboard. */
    @Test
    void listsCategories() throws Exception {
        Category category = categoryRepository.save(new Category("REST List Agile", "Agile principles"));
        // A linked quiz must not leak into (or recurse through) the category JSON.
        Quiz quiz = new Quiz("REST List Quiz", "Linked quiz", "RL101", true);
        quiz.setCategory(category);
        quizRepository.save(quiz);

        mockMvc.perform(get("/api/categories").header("Origin", "http://localhost:5173"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "*"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@.name == 'REST List Agile')].id").value(category.getId().intValue()))
                .andExpect(jsonPath("$[?(@.name == 'REST List Agile')].description").value("Agile principles"))
                .andExpect(jsonPath("$[?(@.name == 'REST List Agile')].quizzes").doesNotExist());
    }

    /** Returns one category by id. */
    @Test
    void getsCategoryById() throws Exception {
        Category category = categoryRepository.save(new Category("REST Single Programming", "Programming concepts"));

        mockMvc.perform(get("/api/categories/{id}", category.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.getId().intValue()))
                .andExpect(jsonPath("$.name").value("REST Single Programming"))
                .andExpect(jsonPath("$.description").value("Programming concepts"));
    }

    /** Unknown ids yield 404 Not Found. */
    @Test
    void missingCategoryReturns404() throws Exception {
        mockMvc.perform(get("/api/categories/{id}", 999_999L))
                .andExpect(status().isNotFound());
    }
}
