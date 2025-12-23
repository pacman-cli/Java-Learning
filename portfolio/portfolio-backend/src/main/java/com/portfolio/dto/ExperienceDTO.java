package com.portfolio.dto;

import java.util.List;

public class ExperienceDTO {
    private Long id;
    private String jobTitle;
    private String company;
    private String location;
    private String period;
    private String description;
    private List<String> technologies;

    // Constructors
    public ExperienceDTO() {
    }

    public ExperienceDTO(Long id, String jobTitle, String company, String location,
            String period, String description, List<String> technologies) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.company = company;
        this.location = location;
        this.period = period;
        this.description = description;
        this.technologies = technologies;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
    }
}