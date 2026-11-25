package com.DevElite.Quiz;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.DevElite.Quiz.domain.*;
import com.DevElite.Quiz.service.QuizService;

/** Teacher navigation and category assignment integration coverage for Ex 6–7. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class QuizCategoryManagementTests {
    @Autowired private MockMvc mvc;
    @Autowired private CategoryRepository categories;
    @Autowired private QuizRepository quizzes;
    @Autowired private QuizService service;
    @Autowired private QuizApplication application;

    @Test
    void teacherListsHaveNavigationAndCategoryDetails() throws Exception {
        Category category = categories.save(new Category("Security", "Security fundamentals"));
        Quiz quiz = new Quiz("Security Quiz", "Secure code", "SEC101", true);
        quiz.setCategory(category);
        service.saveQuiz(quiz);
        for (String path : new String[] {"/quizzes", "/categories"}) {
            mvc.perform(get(path)).andExpect(status().isOk())
                .andExpect(content().string(containsString("href=\"/quizzes\"")))
                .andExpect(content().string(containsString("href=\"/categories\"")));
        }
        mvc.perform(get("/categories"))
            .andExpect(content().string(containsString("Security fundamentals")));
        mvc.perform(get("/quizzes"))
            .andExpect(content().string(matchesPattern("(?s).*Security Quiz</td>.*?<td>Security</td>.*")));
    }

    @Test
    void createsReassignsAndClearsQuizCategory() throws Exception {
        Category first = categories.save(new Category("First topic", null));
        Category second = categories.save(new Category("Second topic", null));
        mvc.perform(get("/quizzes/add")).andExpect(status().isOk())
            .andExpect(model().attributeExists("categories"))
            .andExpect(content().string(containsString("<option value=\"\">Unassigned</option>")));
        mvc.perform(post("/quizzes/add").param("name", "Category flow quiz")
                .param("courseCode", "CAT101").param("category.id", first.getId().toString()))
            .andExpect(redirectedUrl("/quizzes"));
        Quiz quiz = quizzes.findByName("Category flow quiz").get(0);
        assertEquals(first.getId(), quiz.getCategory().getId());
        var createdAt = quiz.getCreatedAt();
        mvc.perform(get("/quizzes/edit/" + quiz.getId())).andExpect(status().isOk())
            .andExpect(content().string(matchesPattern("(?s).*<option[^>]*value=\"" + first.getId() + "\"[^>]*selected=\"selected\"[^>]*>First topic</option>.*")));
        mvc.perform(post("/quizzes/edit/" + quiz.getId()).param("name", "Category flow quiz")
                .param("category.id", second.getId().toString()))
            .andExpect(redirectedUrl("/quizzes"));
        assertEquals(second.getId(), quizzes.findById(quiz.getId()).orElseThrow().getCategory().getId());
        mvc.perform(post("/quizzes/edit/" + quiz.getId()).param("name", "Category flow quiz")
                .param("category.id", ""))
            .andExpect(redirectedUrl("/quizzes"));
        assertNull(quizzes.findById(quiz.getId()).orElseThrow().getCategory());
        assertEquals(createdAt, quizzes.findById(quiz.getId()).orElseThrow().getCreatedAt());
        mvc.perform(get("/quizzes")).andExpect(content().string(matchesPattern("(?s).*Category flow quiz</td>.*?<td>Uncategorized</td>.*")));
    }

    @Test
    void rejectsInvalidCategoriesWithoutSaving() throws Exception {
        long count = quizzes.count();
        for (String invalid : new String[] {"-1", "not-an-id"}) {
            mvc.perform(post("/quizzes/add").param("name", "Invalid category quiz").param("category.id", invalid))
                .andExpect(status().isOk()).andExpect(view().name("add-quiz"))
                .andExpect(model().attributeHasFieldErrors("quiz", "category.id"));
        }
        assertEquals(count, quizzes.count());
        Quiz quiz = service.saveQuiz(new Quiz("Unassigned quiz", null, "CAT101", false));
        mvc.perform(post("/quizzes/edit/" + quiz.getId()).param("name", "Should not change").param("category.id", "-1"))
            .andExpect(view().name("edit-quiz"))
            .andExpect(model().attributeExists("categories"));
        assertEquals("Unassigned quiz", quizzes.findById(quiz.getId()).orElseThrow().getName());
    }

    @Test
    void emptyCategoryListAndSeededQuizAssignments() throws Exception {
        quizzes.deleteAll();
        categories.deleteAll();
        mvc.perform(get("/categories")).andExpect(status().isOk())
            .andExpect(content().string(containsString("No categories have been added yet.")));
        application.demoData(quizzes, categories).run();
        assertEquals("Agile", quizzes.findByName("The Scrum Framework").get(0).getCategory().getName());
        assertEquals("Programming", quizzes.findByName("Java Fundamentals").get(0).getCategory().getName());
        assertEquals("Cloud & DevOps", quizzes.findByName("Cloud & DevOps Architecture").get(0).getCategory().getName());
        application.demoData(quizzes, categories).run();
        assertEquals(3, quizzes.count());
        assertEquals(3, categories.count());
    }
}
