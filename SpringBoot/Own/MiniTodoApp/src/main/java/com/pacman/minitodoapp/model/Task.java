package com.pacman.minitodoapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Task {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    public Task() {}
    public Task(String title){
        this.title=title;
    }
    public Long getId(){
        return this.id;
    }
    public String getTitle(){
        return this.title;
    }
    public void setId(Long id){
        this.id=id;
    }
}
