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

@SpringBootTest
class QuizApplicationTests {

	private final AddQuizService addQuizService;
	private final QuizRepository quizRepository;
	private final QuestionRepository questionRepository;
	private final AnswerOptionRepository answerOptionRepository;

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

	@Test
	void contextLoads() {
	}

	@Test
	void addsQuizToDatabase() {
		Quiz quiz = new Quiz("Backend Engineering", "Backend quiz", "BE101", false);

		Quiz savedQuiz = addQuizService.addQuiz(quiz);

		assertNotNull(savedQuiz.getId());
		assertNotNull(savedQuiz.getCreatedAt());
		assertEquals("Backend Engineering", quizRepository.findById(savedQuiz.getId()).orElseThrow().getName());
	}

	@Test
	void testEntityHierarchyAndCascade() {
		// 1. Create and save Quiz
		Quiz quiz = new Quiz("Scrum Framework", "Learn Scrum", "SOF005AS3AE", true);
		Quiz savedQuiz = quizRepository.save(quiz);
		assertNotNull(savedQuiz.getId());

		// 2. Create Question linked to Quiz
		Question question = new Question("What is Sprint Retrospective?", "Normal", savedQuiz);
		Question savedQuestion = questionRepository.save(question);
		assertNotNull(savedQuestion.getId());

		// 3. Create AnswerOptions linked to Question
		AnswerOption opt1 = new AnswerOption("Improve the team's process", true, savedQuestion);
		AnswerOption opt2 = new AnswerOption("Write more code", false, savedQuestion);
		answerOptionRepository.save(opt1);
		answerOptionRepository.save(opt2);

		// 4. Verify query methods
		List<Question> questions = questionRepository.findByQuizId(savedQuiz.getId());
		assertEquals(1, questions.size());

		List<AnswerOption> options = answerOptionRepository.findByQuestionId(savedQuestion.getId());
		assertEquals(2, options.size());

		List<Quiz> publishedQuizzes = quizRepository.findByIsPublishedTrue();
		assertTrue(publishedQuizzes.stream().anyMatch(q -> q.getId().equals(savedQuiz.getId())));

		// 5. Test Cascade Deletion: deleting quiz should delete question and answer options
		quizRepository.delete(savedQuiz);
		assertFalse(quizRepository.findById(savedQuiz.getId()).isPresent());
		assertEquals(0, questionRepository.findByQuizId(savedQuiz.getId()).size());
		assertEquals(0, answerOptionRepository.findByQuestionId(savedQuestion.getId()).size());
	}
}
