package com.DevElite.Quiz.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA persistence for categories used to organize teacher quizzes
 * and support student category browsing.
 */
public interface CategoryRepository extends CrudRepository<Category, Long> {

    /**
     * Finds categories with an exact display name.
     * @param name category name to search for
     * @return matching categories, or an empty list
     */
    List<Category> findByName(String name);
}
