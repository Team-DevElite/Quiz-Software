package com.DevElite.Quiz.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.DevElite.Quiz.domain.AnswerOption;
import com.DevElite.Quiz.domain.AnswerOptionRepository;
import com.DevElite.Quiz.domain.Question;
import com.DevElite.Quiz.domain.QuestionRepository;

@Service
public class AnswerOptionService {

    private final AnswerOptionRepository answerOptionRepository;
    private final QuestionRepository questionRepository;

    public AnswerOptionService(AnswerOptionRepository answerOptionRepository,
                               QuestionRepository questionRepository) {
        this.answerOptionRepository = answerOptionRepository;
        this.questionRepository = questionRepository;
    }

    public List<AnswerOption> getOptionsByQuestionId(Long questionId) {
        return answerOptionRepository.findByQuestionId(questionId);
    }

    public Question getQuestionById(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + questionId));
    }

    @Transactional
    public AnswerOption addOption(Long questionId, AnswerOption option) {
        Question question = getQuestionById(questionId);
        option.setQuestion(question);
        if (option.getIsCorrect() == null) {
            option.setIsCorrect(false);
        }
        return answerOptionRepository.save(option);
    }

    @Transactional
    public void deleteOption(Long id) {
        answerOptionRepository.deleteById(id);
    }
}