package com.DevElite.Quiz;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.domain.QuizRepository;
import com.DevElite.Quiz.service.AddQuizService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class QuizApplicationTests {

	@Autowired
	private AddQuizService addQuizService;

	@Autowired
	private QuizRepository quizRepository;

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
}
