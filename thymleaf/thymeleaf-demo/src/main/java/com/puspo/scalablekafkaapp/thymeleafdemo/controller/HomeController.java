package com.puspo.scalablekafkaapp.thymeleafdemo.controller;

import com.puspo.scalablekafkaapp.thymeleafdemo.service.BlogPostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final BlogPostService blogPostService;

    public HomeController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping("/") // ✅ only one mapping for "/"
    public String home(Model model) {
        model.addAttribute("blogPosts", blogPostService.getAllBlogPosts());
        return "index";
    }
}

