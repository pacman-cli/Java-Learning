package com.portfolio.model;

import jakarta.persistence.*;

@Entity
public class Education {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String school;
    private String degree;
    private String field;
    private Integer startYear;
    private Integer endYear;

    public Education() {}
    public Education(String school, String degree, String field, Integer startYear, Integer endYear) {
        this.school = school; this.degree = degree; this.field = field; this.startYear = startYear; this.endYear = endYear;
    }
    public Long getId() { return id; }
    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }
    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }
    public String getField() { return field; }
    public void setField(String field) { this.field = field; }
    public Integer getStartYear() { return startYear; }
    public void setStartYear(Integer startYear) { this.startYear = startYear; }
    public Integer getEndYear() { return endYear; }
    public void setEndYear(Integer endYear) { this.endYear = endYear; }
}
