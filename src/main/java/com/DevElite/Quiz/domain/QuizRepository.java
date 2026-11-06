package com.DevElite.Quiz.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface QuizRepository extends CrudRepository<Quiz, Long> {
    List<Quiz> findByName(String name);
    List<Quiz> findByIsPublishedTrue();
    List<Quiz> findByCourseCode(String courseCode);
}
