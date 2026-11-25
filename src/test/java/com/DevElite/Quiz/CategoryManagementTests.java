package com.DevElite.Quiz;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.DevElite.Quiz.domain.Category;
import com.DevElite.Quiz.domain.CategoryRepository;
import com.DevElite.Quiz.domain.QuizRepository;
import com.DevElite.Quiz.service.CategoryService;

/** Integration coverage for teacher category creation, validation and demo seeding. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CategoryManagementTests {
    @Autowired private MockMvc mvc;
    @Autowired private CategoryService service;
    @Autowired private CategoryRepository categories;
    @Autowired private QuizRepository quizzes;
    @Autowired private QuizApplication application;

    @Test
    void displaysFormAndPersistsCategoryThenListsIt() throws Exception {
        mvc.perform(get("/categories/add"))
            .andExpect(status().isOk()).andExpect(view().name("add-category"))
            .andExpect(model().attributeExists("category"))
            .andExpect(content().string(containsString("Save Category")));
        mvc.perform(post("/categories/add").param("name", "  Databases  ")
                .param("description", "Relational database fundamentals"))
            .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/categories"));
        Category saved = categories.findByName("Databases").get(0);
        assertEquals("Relational database fundamentals", saved.getDescription());
        assertEquals(saved.getId(), service.getCategoryById(saved.getId()).orElseThrow().getId());
        mvc.perform(get("/categories")).andExpect(status().isOk())
            .andExpect(view().name("categories"))
            .andExpect(content().string(containsString("Databases")));
    }

    @Test
    void rejectsMissingBlankAndOversizedFieldsWithoutSaving() throws Exception {
        long count = categories.count();
        for (String name : new String[] {"", "   ", "a".repeat(256)}) {
            mvc.perform(post("/categories/add").param("name", name).param("description", "Preserved"))
                .andExpect(status().isOk()).andExpect(view().name("add-category"))
                .andExpect(model().attributeHasFieldErrors("category", "name"))
                .andExpect(content().string(containsString("Preserved")));
        }
        mvc.perform(post("/categories/add"))
            .andExpect(model().attributeHasFieldErrors("category", "name"));
        mvc.perform(post("/categories/add").param("name", "Valid").param("description", "a".repeat(256)))
            .andExpect(model().attributeHasFieldErrors("category", "description"));
        assertEquals(count, categories.count());
        assertThrows(IllegalArgumentException.class, () -> service.saveCategory(new Category(" ", null)));
    }

    @Test
    void createsRatherThanOverwritingPostedId() throws Exception {
        Category existing = service.saveCategory(new Category("Existing", "Keep me"));
        mvc.perform(post("/categories/add").param("id", existing.getId().toString())
                .param("name", "New category"))
            .andExpect(redirectedUrl("/categories"));
        assertEquals("Existing", categories.findById(existing.getId()).orElseThrow().getName());
        assertNotEquals(existing.getId(), categories.findByName("New category").get(0).getId());
    }

    @Test
    void seedsCategoriesIndependentlyAndDoesNotDuplicateOnRepeat() throws Exception {
        // Other integration tests may leave categorized quizzes in the shared context.
        // This test rolls back, including these temporary detachments.
        quizzes.findAll().forEach(quiz -> {
            quiz.setCategory(null);
            quizzes.save(quiz);
        });
        categories.deleteAll();
        long quizCount = quizzes.count();
        application.demoData(quizzes, categories).run();
        assertEquals(3, categories.count());
        assertEquals(1, categories.findByName("Agile").size());
        assertEquals(1, categories.findByName("Programming").size());
        application.demoData(quizzes, categories).run();
        assertEquals(3, categories.count());
        assertEquals(quizCount, quizzes.count());
    }
}
