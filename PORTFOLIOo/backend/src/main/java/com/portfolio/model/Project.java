package com.portfolio.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Project {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 2000)
    private String description;
    @ElementCollection
    private List<String> tags;
    private String repoUrl;
    private String demoUrl;
    private String imageUrl;

    public Project() {}
    public Project(String title, String description, List<String> tags, String repoUrl, String demoUrl, String imageUrl) {
        this.title = title; this.description = description; this.tags = tags; this.repoUrl = repoUrl; this.demoUrl = demoUrl; this.imageUrl = imageUrl;
    }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public String getRepoUrl() { return repoUrl; }
    public void setRepoUrl(String repoUrl) { this.repoUrl = repoUrl; }
    public String getDemoUrl() { return demoUrl; }
    public void setDemoUrl(String demoUrl) { this.demoUrl = demoUrl; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
