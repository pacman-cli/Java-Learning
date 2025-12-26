package com.portfolio.dto;

import java.util.List;

public class EducationDTO {
    private Long id;
    private String degree;
    private String institution;
    private String location;
    private String period;
    private String gpa;
    private String description;
    private List<String> courses;

    // Constructors
    public EducationDTO() {
    }

    public EducationDTO(Long id, String degree, String institution, String location,
            String period, String gpa, String description, List<String> courses) {
        this.id = id;
        this.degree = degree;
        this.institution = institution;
        this.location = location;
        this.period = period;
        this.gpa = gpa;
        this.description = description;
        this.courses = courses;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }
}