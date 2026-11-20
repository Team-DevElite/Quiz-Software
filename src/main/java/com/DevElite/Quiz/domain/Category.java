package com.DevElite.Quiz.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * JPA entity grouping quizzes by topic for teacher category management and
 * student category browsing (Sprint 2 category user stories).
 */
@Entity
public class Category {

    /** Automatically generated primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Required display name, for example "Agile". */
    @Column(nullable = false)
    private String name;

    /** Optional explanation of the topics covered by this category. */
    private String description;

    /**
     * Inverse relationship: Quiz.category owns the nullable foreign key.
     * No cascade or orphan removal: quizzes must survive category removal.
     * Hidden from JSON to prevent recursion through Quiz.category.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Quiz> quizzes = new ArrayList<>();

    /** Default constructor required by JPA. */
    public Category() {
    }

    /**
     * Creates a category for persistence.
     * @param name category display name
     * @param description optional topic description
     */
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /** @return generated category identifier */
    public Long getId() {
        return id;
    }

    /** @param id category identifier */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return category display name */
    public String getName() {
        return name;
    }

    /** @param name category display name */
    public void setName(String name) {
        this.name = name;
    }

    /** @return optional topic description */
    public String getDescription() {
        return description;
    }

    /** @param description optional topic description */
    public void setDescription(String description) {
        this.description = description;
    }

    /** @return quizzes in this category; persist assignments through Quiz.setCategory */
    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    /**
     * Replaces the inverse collection; callers must also update Quiz.category.
     * @param quizzes quizzes belonging to this category
     */
    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
}
