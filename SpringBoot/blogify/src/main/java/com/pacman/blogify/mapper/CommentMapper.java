package com.pacman.blogify.mapper;

import com.pacman.blogify.dto.CommentDto;
import com.pacman.blogify.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    
    // Entity to DTO
    public CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        
        CommentDto.CommentDtoBuilder builder = CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt());

        // Set author information if author exists
        if (comment.getAuthor() != null) {
            builder.authorName(comment.getAuthor().getUsername())
                   .authorId(comment.getAuthor().getId());
        }

        // Set blog post information if post exists
        if (comment.getPost() != null) {
            builder.blogPostId(comment.getPost().getId())
                   .blogPostTitle(comment.getPost().getTitle());
        }

        return builder.build();
    }

    // DTO to Entity (for creating new comments)
    public Comment toEntity(CommentDto commentDto) {
        if (commentDto == null) {
            return null;
        }
        
        return Comment.builder()
                .id(commentDto.getId())
                .content(commentDto.getContent())
                .createdAt(commentDto.getCreatedAt())
                // Note: Author and Post should be set separately in the service layer
                // as they require database lookups
                .build();
    }

    // Entity to DTO with blog post information
    public CommentDto toDtoWithPost(Comment comment) {
        if (comment == null) {
            return null;
        }
        
        CommentDto dto = toDto(comment);
        // You can add blog post information here if needed in the DTO
        return dto;
    }

    // Update existing entity with DTO data (for updates)
    public void updateEntityFromDto(Comment comment, CommentDto commentDto) {
        if (comment == null || commentDto == null) {
            return;
        }
        
        comment.setContent(commentDto.getContent());
        // Note: Don't update createdAt, author, or post during updates
        // Only update the content and other mutable fields
    }
}

