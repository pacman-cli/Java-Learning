package com.pacman.taskmanagementsystemadvancedversion.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;
@Entity
@Data
public class User {
    //getter and setters
    @Id @GeneratedValue
    private Long id;
    private String name;
    @OneToMany(mappedBy = "user")
    private List<Task> tasks;

}
