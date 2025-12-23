package com.pacman.blogify.controller;

import com.pacman.blogify.dto.CommentDto;
import com.pacman.blogify.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comment Management", description = "APIs for managing comments on blog posts")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}")
    @Operation(summary = "Create a new comment", description = "Create a new comment on a specific blog post", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<CommentDto> createComment(
            @Parameter(description = "Blog post ID", required = true) @PathVariable Long postId,
            @Valid @RequestBody CommentDto commentDto,
            Authentication authentication) {

        String username = authentication.getName();
        CommentDto createdComment = commentService.createComment(postId, commentDto, username);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}")
    @Operation(summary = "Get comments for a blog post", description = "Retrieve all comments for a specific blog post")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(
            @Parameter(description = "Blog post ID", required = true) @PathVariable Long postId) {

        List<CommentDto> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}")
    @Operation(summary = "Get a specific comment", description = "Retrieve a comment by its ID")
    public ResponseEntity<CommentDto> getCommentById(
            @Parameter(description = "Comment ID", required = true) @PathVariable Long commentId) {

        CommentDto comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "Update a comment", description = "Update an existing comment (only by the author)", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<CommentDto> updateComment(
            @Parameter(description = "Comment ID", required = true) @PathVariable Long commentId,
            @Valid @RequestBody CommentDto commentDto,
            Authentication authentication) {

        String username = authentication.getName();
        CommentDto updatedComment = commentService.updateComment(commentId, commentDto, username);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete a comment", description = "Delete an existing comment (only by the author)", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "Comment ID", required = true) @PathVariable Long commentId,
            Authentication authentication) {

        String username = authentication.getName();
        commentService.deleteComment(commentId, username);
        return ResponseEntity.noContent().build();
    }
}