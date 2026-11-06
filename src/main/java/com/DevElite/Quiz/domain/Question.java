package com.DevElite.Quiz.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

/**
 * Question JPA Entity representing an individual question within a {@link Quiz}.
 * 
 * <p>Each Question contains a prompt (questionText), a difficulty rating ("Easy",
 * "Normal", or "Hard", defaulting to "Normal"), a reference to its parent Quiz,
 * and a list of multiple-choice {@link AnswerOption} records. Deleting a Question
 * cascades to all of its answer options.</p>
 */
@Entity
public class Question {

    /** Primary Key with auto-increment generation. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The actual question text / prompt (e.g., "What is the purpose of the Retrospective?"). */
    private String questionText;

    /** Difficulty tier: "Easy", "Normal", or "Hard". Defaults to "Normal". */
    private String difficulty = "Normal";

    /**
     * The parent Quiz this question belongs to.
     * Uses {@code @OnDelete(action = OnDeleteAction.CASCADE)} to enforce database-level cascade.
     */
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Quiz quiz;

    /**
     * Multiple-choice options associated with this question.
     * Cascades all persistence operations and removes orphaned child records.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerOption> answerOptions = new ArrayList<>();

    /**
     * Default constructor required by JPA.
     */
    public Question() {
    }

    /**
     * Parameterized constructor with question text, difficulty, and parent quiz.
     * 
     * @param questionText The prompt of the question
     * @param difficulty "Easy", "Normal", or "Hard"
     * @param quiz The parent Quiz entity
     */
    public Question(String questionText, String difficulty, Quiz quiz) {
        this.questionText = questionText;
        this.difficulty = (difficulty != null && !difficulty.isBlank()) ? difficulty : "Normal";
        this.quiz = quiz;
    }

    /**
     * Parameterized constructor defaulting difficulty to "Normal".
     * 
     * @param questionText The prompt of the question
     * @param quiz The parent Quiz entity
     */
    public Question(String questionText, Quiz quiz) {
        this(questionText, "Normal", quiz);
    }

    /**
     * Helper method to attach an answer option and maintain bidirectional consistency.
     * 
     * @param option The answer option to add
     */
    public void addAnswerOption(AnswerOption option) {
        this.answerOptions.add(option);
        option.setQuestion(this);
    }

    /**
     * Helper method to remove an answer option and detach its relationship.
     * 
     * @param option The answer option to remove
     */
    public void removeAnswerOption(AnswerOption option) {
        this.answerOptions.remove(option);
        option.setQuestion(null);
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public List<AnswerOption> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<AnswerOption> answerOptions) {
        this.answerOptions = answerOptions;
    }
}
