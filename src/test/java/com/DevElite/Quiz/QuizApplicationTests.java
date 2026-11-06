package com.DevElite.Quiz;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.DevElite.Quiz.domain.AnswerOption;
import com.DevElite.Quiz.domain.AnswerOptionRepository;
import com.DevElite.Quiz.domain.Question;
import com.DevElite.Quiz.domain.QuestionRepository;
import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.domain.QuizRepository;
import com.DevElite.Quiz.service.AddQuizService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test suite for Quizzer backend application.
 * 
 * <p>Tests Spring context loading, service layer persistence, entity relationships,
 * repository query methods, and cascade deletion across the Quiz -> Question -> AnswerOption hierarchy.</p>
 */
@SpringBootTest
class QuizApplicationTests {

	private final AddQuizService addQuizService;
	private final QuizRepository quizRepository;
	private final QuestionRepository questionRepository;
	private final AnswerOptionRepository answerOptionRepository;

	/**
	 * Constructor injection for Spring Boot integration test dependencies.
	 */
	@Autowired
	QuizApplicationTests(AddQuizService addQuizService,
						 QuizRepository quizRepository,
						 QuestionRepository questionRepository,
						 AnswerOptionRepository answerOptionRepository) {
		this.addQuizService = addQuizService;
		this.quizRepository = quizRepository;
		this.questionRepository = questionRepository;
		this.answerOptionRepository = answerOptionRepository;
	}

	/**
	 * Verifies that the Spring Boot ApplicationContext loads successfully.
	 */
	@Test
	void contextLoads() {
	}

	/**
	 * Tests adding a basic Quiz to the database via AddQuizService and verifying its retrieval.
	 */
	@Test
	void addsQuizToDatabase() {
		Quiz quiz = new Quiz("Backend Engineering", "Backend quiz", "BE101", false);

		Quiz savedQuiz = addQuizService.addQuiz(quiz);

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
}
