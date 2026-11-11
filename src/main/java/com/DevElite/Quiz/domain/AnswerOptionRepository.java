package com.DevElite.Quiz.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA Repository for managing {@link AnswerOption} persistence.
 */
public interface AnswerOptionRepository extends CrudRepository<AnswerOption, Long> {

    /**
     * Retrieve all answer choices for a given Question entity.
     * 
     * @param question The parent Question entity
     * @return List of answer options for the question
     */
    List<AnswerOption> findByQuestion(Question question);

    /**
     * Retrieve all answer choices by the parent Question's primary key ID.
     * 
     * @param questionId ID of the parent question
     * @return List of answer options for the question
     */
    List<AnswerOption> findByQuestionId(Long questionId);
}
