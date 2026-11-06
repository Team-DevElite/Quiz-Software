package com.DevElite.Quiz.service;

import org.springframework.stereotype.Service;

import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.domain.QuizRepository;

@Service
public class AddQuizService {

    private final QuizRepository quizRepository;

    public AddQuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz addQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }
}
