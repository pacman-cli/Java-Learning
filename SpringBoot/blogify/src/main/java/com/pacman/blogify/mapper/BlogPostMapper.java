package com.pacman.blogify.mapper;

import com.pacman.blogify.dto.BlogPostDto;
import com.pacman.blogify.model.BlogPost;
import org.springframework.stereotype.Component;

@Component // 👈 This makes it a Spring bean
public class BlogPostMapper {
    //Entity to DTO
    public BlogPostDto toDto(BlogPost blogPost) {
        if (blogPost == null) {
            return null;
        }
        BlogPostDto dto = BlogPostDto.builder()
                .id(blogPost.getId())
                .title(blogPost.getTitle())
                .content(blogPost.getContent())
                .author(blogPost.getAuthor().getUsername())
                .createdAt(blogPost.getCreatedAt())
                .updatedAt(blogPost.getUpdatedAt())
                .build();

        if (blogPost.getAuthor() != null) {
            dto.setAuthor(blogPost.getAuthor().getUsername());
        }
        return dto;
    }

    //Dto to Entity
    public BlogPost toEntity(BlogPostDto blogPostDto) {
        if (blogPostDto == null) {
            return null;
        }
        return BlogPost.builder()
                .id(blogPostDto.getId())
                .title(blogPostDto.getTitle()) //// author, id, timestamps will be set in service
                .content(blogPostDto.getContent())
                .build();

    }

}

