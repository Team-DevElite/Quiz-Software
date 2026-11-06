package com.DevElite.Quiz.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class Quiz {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String courseCode;
    private Boolean isPublished = false;

    public Quiz() {
    }

    public Quiz(String name, String description, String courseCode, Boolean isPublished) {
        this.name = name;
        this.description = description;
        this.courseCode = courseCode;
        this.isPublished = isPublished;
    }

    public Quiz(Long id, String name, String description, String courseCode, Boolean isPublished) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.courseCode = courseCode;
        this.isPublished = isPublished;
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
}
