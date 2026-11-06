package com.DevElite.Quiz.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * AnswerOption JPA Entity representing an answer choice for a {@link Question}.
 * 
 * <p>
 * Each AnswerOption contains option text and an isCorrect boolean flag
 * indicating
 * whether this choice is the correct answer to the question. A Question may
 * have
 * multiple AnswerOptions.
 * </p>
 */
@Entity
public class AnswerOption {

    /** Primary Key with auto-increment generation. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The display text of the answer option (e.g., "Finding ways to improve the
     * process").
     */
    private String optionText;

    /**
     * Correctness indicator: true if this is the correct answer, false otherwise.
     * Defaults to false.
     */
    private Boolean isCorrect = false;

    /**
     * The parent Question this answer option belongs to.
     * Uses {@code @OnDelete(action = OnDeleteAction.CASCADE)} to enforce
     * database-level cascade.
     */
    @ManyToOne
    @JoinColumn(name = "question_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Question question;

    /**
     * Default constructor required by JPA.
     */
    public AnswerOption() {
    }

    /**
     * Parameterized constructor for creating an answer option.
     * 
     * @param optionText The display text of the answer choice
     * @param isCorrect  Whether this choice is the correct answer
     * @param question   The parent Question entity
     */
    public AnswerOption(String optionText, Boolean isCorrect, Question question) {
        this.optionText = optionText;
        this.isCorrect = (isCorrect != null) ? isCorrect : false;
        this.question = question;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
