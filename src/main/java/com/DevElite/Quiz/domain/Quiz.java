package com.DevElite.Quiz.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Quiz JPA Entity representing an assessment topic created by a teacher.
 * 
 * <p>A Quiz consists of multiple {@link Question} entities and contains metadata
 * such as name, description, course code, published status, and creation date.
 * Deleting a Quiz will cascade-delete all of its associated questions.</p>
 */
@Entity
public class Quiz {

    /** Primary Key with auto-increment generation. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Title/name of the quiz (e.g., "The Scrum Framework"). */
    private String name;

    /** Detailed explanation of the quiz topic or learning objectives. */
    private String description;

    /** Institutional course identifier (e.g., "SOF005AS3AE"). */
    private String courseCode;

    /** 
     * Publication status. Only quizzes with isPublished = true are visible 
     * to students in the student dashboard. Default is false.
     */
    private Boolean isPublished = false;

    /** Date the quiz was added, initialized to current date upon creation. */
    private LocalDate createdAt = LocalDate.now();

    /**
     * Questions belonging to this quiz.
     * 
     * <ul>
     *   <li>{@code cascade = CascadeType.ALL}: Persisting/removing a quiz cascades to its questions.</li>
     *   <li>{@code orphanRemoval = true}: Removing a question from this collection deletes it from the database.</li>
     *   <li>{@code @JsonIgnore}: Prevents infinite recursion when serializing to JSON for REST API.</li>
     * </ul>
     */
    @JsonIgnore
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    /**
     * Default constructor required by JPA specification.
     */
    public Quiz() {
    }

    /**
     * Parameterized constructor for creating a quiz with required metadata.
     * 
     * @param name Name of the quiz
     * @param description Brief description of the quiz
     * @param courseCode Associated course code
     * @param isPublished Whether the quiz is immediately visible to students
     */
    public Quiz(String name, String description, String courseCode, Boolean isPublished) {
        this.name = name;
        this.description = description;
        this.courseCode = courseCode;
        this.isPublished = (isPublished != null) ? isPublished : false;
        this.createdAt = LocalDate.now();
    }

    /**
     * Constructor allowing explicit creation date specification.
     * 
     * @param name Name of the quiz
     * @param description Brief description of the quiz
     * @param courseCode Associated course code
     * @param isPublished Publication status
     * @param createdAt Creation date
     */
    public Quiz(String name, String description, String courseCode, Boolean isPublished, LocalDate createdAt) {
        this.name = name;
        this.description = description;
        this.courseCode = courseCode;
        this.isPublished = (isPublished != null) ? isPublished : false;
        this.createdAt = (createdAt != null) ? createdAt : LocalDate.now();
    }

    /**
     * Full constructor including ID (typically for testing or database mapping).
     */
    public Quiz(Long id, String name, String description, String courseCode, Boolean isPublished) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.courseCode = courseCode;
        this.isPublished = (isPublished != null) ? isPublished : false;
        this.createdAt = LocalDate.now();
    }

    /**
     * Convenience helper method to add a question and synchronize both sides of the relationship.
     * 
     * @param question The question to attach to this quiz
     */
    public void addQuestion(Question question) {
        this.questions.add(question);
        question.setQuiz(this);
    }

    /**
     * Convenience helper method to remove a question and synchronize both sides of the relationship.
     * 
     * @param question The question to remove from this quiz
     */
    public void removeQuestion(Question question) {
        this.questions.remove(question);
        question.setQuiz(null);
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    /** Backward-compatibility getter for snake_case callers. */
    public String getCourse_code() {
        return courseCode;
    }

    /** Backward-compatibility setter for snake_case callers. */
    public void setCourse_code(String course_code) {
        this.courseCode = course_code;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
