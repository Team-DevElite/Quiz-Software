package com.DevElite.Quiz.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA Repository for managing {@link Quiz} persistence.
 */
public interface QuizRepository extends CrudRepository<Quiz, Long> {

    /**
     * Find quizzes by exact title match.
     * 
     * @param name The quiz name to search for
     * @return List of matching Quiz entities
     */
    List<Quiz> findByName(String name);

    /**
     * Find all quizzes that have been published by teachers.
     * Used by the Student Dashboard REST API to display active quizzes.
     * 
     * @return List of published quizzes
     */
    List<Quiz> findByIsPublishedTrue();

    /**
     * Find quizzes associated with a specific course code.
     * 
     * @param courseCode The institutional course code (e.g., "SOF005AS3AE")
     * @return List of matching Quiz entities
     */
    List<Quiz> findByCourseCode(String courseCode);
}
