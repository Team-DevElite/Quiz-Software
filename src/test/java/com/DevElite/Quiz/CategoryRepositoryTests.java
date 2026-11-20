package com.DevElite.Quiz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.DevElite.Quiz.domain.Category;
import com.DevElite.Quiz.domain.CategoryRepository;
import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.domain.QuizRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Persistence and serialization checks for the category domain foundation. */
@DataJpaTest
class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private TestEntityManager entityManager;

    /** Verifies generated IDs, exact-name lookup, updates and deletion. */
    @Test
    void categoryCrud() {
        Category category = categoryRepository.save(new Category("Agile", "Agile principles"));
        entityManager.flush();
        entityManager.clear();

        assertNotNull(category.getId());
        Category loaded = categoryRepository.findByName("Agile").get(0);
        assertEquals("Agile principles", loaded.getDescription());
        loaded.setName("Agile Methods");
        loaded.setDescription("Scrum and Kanban");
        categoryRepository.save(loaded);
        entityManager.flush();
        entityManager.clear();

        assertTrue(categoryRepository.findByName("Agile").isEmpty());
        assertEquals("Scrum and Kanban", categoryRepository.findById(category.getId()).orElseThrow().getDescription());
        categoryRepository.deleteById(category.getId());
        entityManager.flush();
        assertFalse(categoryRepository.existsById(category.getId()));
    }

    /** Verifies both relationship directions and explicit detachment before deletion. */
    @Test
    void persistsCategoryAssignmentAndPreservesDetachedQuiz() {
        Category category = categoryRepository.save(new Category("Programming", "Programming concepts"));
        Quiz quiz = new Quiz("Java Fundamentals", "Core Java", "CS101", true);
        quiz.setCategory(category);
        quizRepository.save(quiz);
        entityManager.flush();
        entityManager.clear();

        Category loaded = categoryRepository.findById(category.getId()).orElseThrow();
        assertEquals(1, loaded.getQuizzes().size());
        Quiz loadedQuiz = loaded.getQuizzes().get(0);
        assertEquals(quiz.getId(), loadedQuiz.getId());
        assertEquals(category.getId(), loadedQuiz.getCategory().getId());
        loadedQuiz.setCategory(null);
        entityManager.flush();
        categoryRepository.delete(loaded);
        entityManager.flush();
        entityManager.clear();

        assertFalse(categoryRepository.existsById(category.getId()));
        assertNull(quizRepository.findById(quiz.getId()).orElseThrow().getCategory());
    }

    /** Verifies category JSON excludes its inverse collection even when populated. */
    @Test
    void categoryJsonDoesNotRecurse() throws Exception {
        Category category = new Category("Agile", "Agile principles");
        Quiz quiz = new Quiz("Scrum", "Scrum framework", "AG101", true);
        quiz.setCategory(category);
        category.getQuizzes().add(quiz);

        var json = new ObjectMapper().readTree(new ObjectMapper().writeValueAsString(category));
        assertEquals("Agile", json.get("name").asText());
        assertFalse(json.has("quizzes"));
    }
}
