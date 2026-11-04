package com.DevElite.Quiz.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
/*
- Quiz
    - id: Long number
    - name: string e.g Backend Engineering
    - description: String
    - Course Code: String
    - published status: Boolean i.e. True or False
*/
import jakarta.persistence.Id;
@Entity 
public class Quiz {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String course_code;
    private Boolean isPublished;


    public Quiz() {
    }


    public Quiz(String name, String description, String course_code, Boolean isPublished) {
        this.name = name;
        this.description = description;
        this.course_code = course_code;
        this.isPublished = isPublished;
    }


    public Quiz(Long id, String name, String description, String course_code, Boolean isPublished) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.course_code = course_code;
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


    public String getCourse_code() {
        return course_code;
    }


    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }


    public Boolean getIsPublished() {
        return isPublished;
    }


    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    

}
