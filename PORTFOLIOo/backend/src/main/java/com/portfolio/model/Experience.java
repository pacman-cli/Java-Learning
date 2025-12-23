package com.portfolio.model;

import jakarta.persistence.*;

@Entity
public class Experience {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String company;
    private String role;
    private String startDate;
    private String endDate;
    @Column(length = 2000)
    private String description;

    public Experience() {}
    public Experience(String company, String role, String startDate, String endDate, String description) {
        this.company = company; this.role = role; this.startDate = startDate; this.endDate = endDate; this.description = description;
    }
    public Long getId() { return id; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
