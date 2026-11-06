package com.DevElite.Quiz.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA Repository for managing {@link Question} persistence.
 */
public interface QuestionRepository extends CrudRepository<Question, Long> {

    /**
     * Retrieve all questions belonging to a given Quiz entity.
     * 
     * @param quiz The parent Quiz entity
     * @return List of questions attached to the quiz
     */
    List<Question> findByQuiz(Quiz quiz);

    /**
     * Retrieve all questions by the parent Quiz's primary key ID.
     * 
     * @param quizId ID of the parent quiz
     * @return List of questions belonging to the quiz
     */
    List<Question> findByQuizId(Long quizId);
}
