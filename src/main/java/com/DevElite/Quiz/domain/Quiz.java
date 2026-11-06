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

@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String courseCode;
    private Boolean isPublished = false;
    private LocalDate createdAt = LocalDate.now();

    @JsonIgnore
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    public Quiz() {
    }

    public Quiz(String name, String description, String courseCode, Boolean isPublished) {
        this.name = name;
        this.description = description;
        this.courseCode = courseCode;
        this.isPublished = (isPublished != null) ? isPublished : false;
        this.createdAt = LocalDate.now();
    }

    public Quiz(String name, String description, String courseCode, Boolean isPublished, LocalDate createdAt) {
        this.name = name;
        this.description = description;
        this.courseCode = courseCode;
        this.isPublished = (isPublished != null) ? isPublished : false;
        this.createdAt = (createdAt != null) ? createdAt : LocalDate.now();
    }

    public Quiz(Long id, String name, String description, String courseCode, Boolean isPublished) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.courseCode = courseCode;
        this.isPublished = (isPublished != null) ? isPublished : false;
        this.createdAt = LocalDate.now();
    }

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

    public String getCourse_code() {
        return courseCode;
    }

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
