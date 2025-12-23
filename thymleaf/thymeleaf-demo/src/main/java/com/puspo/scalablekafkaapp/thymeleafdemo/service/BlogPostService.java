package com.puspo.scalablekafkaapp.thymeleafdemo.service;


import com.puspo.scalablekafkaapp.thymeleafdemo.model.BlogPost;
import com.puspo.scalablekafkaapp.thymeleafdemo.repository.BlogPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogPostService {

    private final BlogPostRepository repository;

    public BlogPostService(BlogPostRepository repository) {
        this.repository = repository;
    }

    public List<BlogPost> getAllBlogPosts() {
        return repository.findAll(); // make sure this returns actual posts
    }
}

