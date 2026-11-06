package com.DevElite.Quiz;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.DevElite.Quiz.domain.AnswerOption;
import com.DevElite.Quiz.domain.AnswerOptionRepository;
import com.DevElite.Quiz.domain.Question;
import com.DevElite.Quiz.domain.QuestionRepository;
import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.domain.QuizRepository;
import com.DevElite.Quiz.service.QuizService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Integration test suite for Quizzer backend application.
 * 
 * <p>Tests Spring context loading, service layer persistence, entity relationships,
 * repository query methods, cascade deletion, and web controller endpoints.</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
class QuizApplicationTests {

	private final QuizService quizService;
	private final QuizRepository quizRepository;
	private final QuestionRepository questionRepository;
	private final AnswerOptionRepository answerOptionRepository;
	private final MockMvc mockMvc;

	/**
	 * Constructor injection for Spring Boot integration test dependencies.
	 */
	@Autowired
	QuizApplicationTests(QuizService quizService,
						 QuizRepository quizRepository,
						 QuestionRepository questionRepository,
						 AnswerOptionRepository answerOptionRepository,
						 MockMvc mockMvc) {
		this.quizService = quizService;
		this.quizRepository = quizRepository;
		this.questionRepository = questionRepository;
		this.answerOptionRepository = answerOptionRepository;
		this.mockMvc = mockMvc;
	}

	/**
	 * Verifies that the Spring Boot ApplicationContext loads successfully.
	 */
	@Test
	void contextLoads() {
	}

	/**
	 * Tests adding a basic Quiz to the database via QuizService and verifying its retrieval.
	 */
	@Test
	void addsQuizToDatabase() {
		Quiz quiz = new Quiz("Backend Engineering", "Backend quiz", "BE101", false);

		Quiz savedQuiz = quizService.saveQuiz(quiz);

		assertNotNull(savedQuiz.getId());
		assertNotNull(savedQuiz.getCreatedAt());
		assertEquals("Backend Engineering", quizRepository.findById(savedQuiz.getId()).orElseThrow().getName());
	}

	/**
	 * Tests the full three-tier domain hierarchy (Quiz -> Question -> AnswerOption),
	 * bidirectional associations, query methods, and cascade deletion.
	 */
	@Test
	void testEntityHierarchyAndCascade() {
		// 1. Create Quiz entity
		Quiz quiz = new Quiz("Scrum Framework", "Learn Scrum", "SOF005AS3AE", true);

		// 2. Create Question entity and attach to Quiz
		Question question = new Question("What is Sprint Retrospective?", "Normal", quiz);
		quiz.addQuestion(question);

		// 3. Create AnswerOption entities and attach to Question
		AnswerOption opt1 = new AnswerOption("Improve the team's process", true, question);
		AnswerOption opt2 = new AnswerOption("Write more code", false, question);
		question.addAnswerOption(opt1);
		question.addAnswerOption(opt2);

		// 4. Save via root Quiz (CascadeType.ALL automatically cascades to Question and AnswerOption)
		Quiz savedQuiz = quizRepository.save(quiz);
		assertNotNull(savedQuiz.getId());
		Long savedQuizId = savedQuiz.getId();

		// Verify that questions were persisted via cascade
		List<Question> questions = questionRepository.findByQuizId(savedQuizId);
		assertEquals(1, questions.size());
		Long savedQuestionId = questions.get(0).getId();

		// Verify that answer options were persisted via cascade
		List<AnswerOption> options = answerOptionRepository.findByQuestionId(savedQuestionId);
		assertEquals(2, options.size());

		// Verify custom repository query for published quizzes
		List<Quiz> publishedQuizzes = quizRepository.findByIsPublishedTrue();
		assertTrue(publishedQuizzes.stream().anyMatch(q -> q.getId().equals(savedQuizId)));

		// 5. Test Cascade Deletion: deleting the root Quiz cascades deletion down to Question and AnswerOption
		quizRepository.delete(savedQuiz);

		assertFalse(quizRepository.findById(savedQuizId).isPresent());
		assertEquals(0, questionRepository.findByQuizId(savedQuizId).size());
		assertEquals(0, answerOptionRepository.findByQuestionId(savedQuestionId).size());
	}

	/**
	 * Tests QuizController endpoints (GET / and GET /quizzes) for User Story 2.
	 * Verifies HTTP 200 OK, view name "quizzes", and model attribute "quizzes".
	 */
	@Test
	void testQuizListEndpoints() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("quizzes"))
				.andExpect(model().attributeExists("quizzes"));

		mockMvc.perform(get("/quizzes"))
				.andExpect(status().isOk())
				.andExpect(view().name("quizzes"))
				.andExpect(model().attributeExists("quizzes"));
	}

	/**
	 * Tests QuizController endpoints for creating a Quiz (User Story 1):
	 * - GET /quizzes/add displays the empty form.
	 * - POST /quizzes/add saves the quiz and redirects to /quizzes.
	 */
	@Test
	void testAddQuizEndpoints() throws Exception {
		// 1. Verify GET /quizzes/add
		mockMvc.perform(get("/quizzes/add"))
				.andExpect(status().isOk())
				.andExpect(view().name("add-quiz"))
				.andExpect(model().attributeExists("quiz"));

		// 2. Verify POST /quizzes/add
		mockMvc.perform(post("/quizzes/add")
				.param("name", "Software Architecture")
				.param("courseCode", "SW101")
				.param("description", "Design patterns and clean code")
				.param("isPublished", "true"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/quizzes"));

		// 3. Verify quiz is in the database
		List<Quiz> matches = quizRepository.findByName("Software Architecture");
		assertFalse(matches.isEmpty());
		assertEquals("SW101", matches.get(0).getCourseCode());
		assertTrue(matches.get(0).getIsPublished());
		assertNotNull(matches.get(0).getCreatedAt());
	}

	/**
	 * Tests QuizController endpoints for editing a Quiz (User Story 3):
	 * - GET /quizzes/edit/{id} loads the edit form pre-populated.
	 * - POST /quizzes/edit/{id} updates the quiz attributes and redirects.
	 */
	@Test
	void testEditQuizEndpoints() throws Exception {
		// 1. Seed a quiz to edit
		Quiz quiz = quizService.saveQuiz(new Quiz("DevOps Basics", "Intro to CI/CD", "DO101", false));
		Long quizId = quiz.getId();

		// 2. Verify GET /quizzes/edit/{id}
		mockMvc.perform(get("/quizzes/edit/" + quizId))
				.andExpect(status().isOk())
				.andExpect(view().name("edit-quiz"))
				.andExpect(model().attributeExists("quiz"));

		// 3. Verify POST /quizzes/edit/{id} updates values
		mockMvc.perform(post("/quizzes/edit/" + quizId)
				.param("name", "Advanced DevOps")
				.param("courseCode", "DO202")
				.param("description", "Kubernetes and GitOps")
				.param("isPublished", "true"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/quizzes"));

		// 4. Verify database reflects changes
		Quiz updated = quizService.getQuizById(quizId).orElseThrow();
		assertEquals("Advanced DevOps", updated.getName());
		assertEquals("DO202", updated.getCourseCode());
		assertEquals("Kubernetes and GitOps", updated.getDescription());
		assertTrue(updated.getIsPublished());
	}

	/**
	 * Tests QuizController endpoint for deleting a Quiz (User Story 4):
	 * - GET /quizzes/delete/{id} removes the quiz and redirects to /quizzes.
	 */
	@Test
	void testDeleteQuizEndpoint() throws Exception {
		// 1. Seed a quiz to delete
		Quiz quiz = quizService.saveQuiz(new Quiz("Temporary Quiz", "To be deleted", "TMP001", false));
		Long quizId = quiz.getId();
		assertTrue(quizRepository.findById(quizId).isPresent());

		// 2. Perform GET /quizzes/delete/{id}
		mockMvc.perform(get("/quizzes/delete/" + quizId))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/quizzes"));

		// 3. Verify quiz is removed from database
		assertFalse(quizRepository.findById(quizId).isPresent());
	}
}
