package com.pacman.blogify.controller;

import com.pacman.blogify.dto.BlogPostDto;
import com.pacman.blogify.service.BlogPostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@SecurityRequirement(name = "Bearer Authentication") // 👈 this is required to secure this endpoint in swagger ui
public class BlogPostController {
    private final BlogPostService blogPostService;

    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping
    public ResponseEntity<List<BlogPostDto>> getAllBlogPosts() {
        return ResponseEntity.ok(blogPostService.getAllBlogPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostDto> getBlogPostById(@PathVariable Long id) {
        return ResponseEntity.ok(blogPostService.getBlogPostById(id));
    }

    @PostMapping
    public ResponseEntity<BlogPostDto> createPost(@Valid @RequestBody BlogPostDto postDto) {
        return ResponseEntity.ok(blogPostService.createPost(postDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPostDto> updatePost(@PathVariable Long id, @Valid @RequestBody BlogPostDto postDto) {
        return ResponseEntity.ok(blogPostService.updatePost(id, postDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        blogPostService.deletePost(id);
        return ResponseEntity.ok("Post deleted successfully");
    }
}
