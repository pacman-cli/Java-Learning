package com.puspo.scalablekafkaapp.thymeleafdemo.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String content;

    private LocalDateTime createdAt;

    // Optional: short summary
    private String summary;

    // Constructors, getters, setters
    public BlogPost() {}

    public BlogPost(String title, String content, String summary, LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    // ...
}
