package com.DevElite.Quiz;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.domain.QuizRepository;
import com.DevElite.Quiz.service.AddQuizService;
import com.DevElite.Quiz.web.QuestionController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class QuizApplicationTests {

	@Autowired
	private AddQuizService addQuizService;

	@Autowired
	private QuizRepository quizRepository;

	@Autowired
	private QuestionController questionController;

	@Test
	void contextLoads() {
	}

	@Test
	void addsQuizToDatabase() {
		Quiz quiz = new Quiz("Backend Engineering", "Backend quiz", "BE101", false);

		Quiz savedQuiz = addQuizService.addQuiz(quiz);

		assertNotNull(savedQuiz.getId());
		assertEquals("Backend Engineering", quizRepository.findById(savedQuiz.getId()).orElseThrow().getName());
	}

	@Test
	void deletesQuizFromDatabase() {
		Quiz savedQuiz = addQuizService.addQuiz(new Quiz("Delete me", "Quiz to delete", "BE102", false));

		var response = questionController.deleteQuiz(savedQuiz.getId());

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertFalse(quizRepository.existsById(savedQuiz.getId()));
	}

	@Test
	void returnsNotFoundWhenDeletingMissingQuiz() {
		var response = questionController.deleteQuiz(Long.MAX_VALUE);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
}
