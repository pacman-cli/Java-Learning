package com.pacman.blogify.service;

import com.pacman.blogify.dto.BlogPostDto;
import com.pacman.blogify.exception.ResourceNotFoundException;
import com.pacman.blogify.exception.UnauthorizedException;
import com.pacman.blogify.mapper.BlogPostMapper;
import com.pacman.blogify.model.BlogPost;
import com.pacman.blogify.model.User;
import com.pacman.blogify.repository.BlogPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogPostService {
    private final BlogPostRepository blogPostRepository;
    private final BlogPostMapper blogPostMapper;
    private final UserService userService;

    public BlogPostService(BlogPostRepository blogPostRepository,
            BlogPostMapper blogPostMapper, UserService userService) {
        this.blogPostRepository = blogPostRepository;
        this.blogPostMapper = blogPostMapper;
        this.userService = userService;
    }

    private User getCurrentUser() {
        return userService.getCurrentUser();
    }

    public List<BlogPostDto> getAllBlogPosts() {
        return blogPostRepository.findAllWithAuthor()
                .stream()
                .map(blogPostMapper::toDto)
                .collect(java.util.stream.Collectors.toList());
    }

    public BlogPostDto getBlogPostById(Long id) {
        return blogPostRepository.findByIdWithAuthor(id)
                .map(blogPostMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post not found with id: " + id));
    }

    public BlogPostDto createPost(BlogPostDto blogPostDto) {
        User currentUser = getCurrentUser();

        BlogPost blogPost = blogPostMapper.toEntity(blogPostDto);
        blogPost.setAuthor(currentUser);

        BlogPost save = blogPostRepository.save(blogPost);
        return blogPostMapper.toDto(save);
    }

    public BlogPostDto updatePost(Long id, BlogPostDto blogPostDto) {
        BlogPost existingPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post not found with id: " + id));

        User currentUser = getCurrentUser();
        if (!existingPost.getAuthor().equals(currentUser)) {
            throw new UnauthorizedException("You don't have permission to update this post");
        }
        existingPost.setTitle(blogPostDto.getTitle());
        existingPost.setContent(blogPostDto.getContent());

        BlogPost saved = blogPostRepository.save(existingPost);
        return blogPostMapper.toDto(saved);
    }

    public void deletePost(Long id) {
        BlogPost post = blogPostRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        User currentUser = getCurrentUser();
        if (!post.getAuthor().equals(currentUser)) {
            throw new UnauthorizedException("You don't have permission to delete this post");
        }

        blogPostRepository.delete(post);
    }
}
