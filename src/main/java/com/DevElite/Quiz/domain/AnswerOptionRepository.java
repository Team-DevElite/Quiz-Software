package com.DevElite.Quiz.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface AnswerOptionRepository extends CrudRepository<AnswerOption, Long> {
    List<AnswerOption> findByQuestion(Question question);
    List<AnswerOption> findByQuestionId(Long questionId);
}

