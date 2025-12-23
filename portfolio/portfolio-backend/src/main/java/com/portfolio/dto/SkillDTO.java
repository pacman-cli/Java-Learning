package com.portfolio.dto;

public class SkillDTO {
    private Long id;
    private String name;
    private Integer level;
    private String category;
    private String icon;

    // Constructors
    public SkillDTO() {
    }

    public SkillDTO(Long id, String name, Integer level, String category, String icon) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.category = category;
        this.icon = icon;
    }

    // Getters and Setters
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}