package com.pacman.blogify.service;

import com.pacman.blogify.dto.CommentDto;
import com.pacman.blogify.exception.ResourceNotFoundException;
import com.pacman.blogify.exception.UnauthorizedException;
import com.pacman.blogify.mapper.CommentMapper;
import com.pacman.blogify.model.BlogPost;
import com.pacman.blogify.model.Comment;
import com.pacman.blogify.model.User;
import com.pacman.blogify.repository.BlogPostRepository;
import com.pacman.blogify.repository.CommentRepository;
import com.pacman.blogify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public CommentDto createComment(Long postId, CommentDto commentDto, String authorUsername) {
        // Find the blog post
        BlogPost blogPost = blogPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post not found with id: " + postId));

        // Find the author
        User author = userRepository.findByUsername(authorUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + authorUsername));

        // Create comment entity
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setPost(blogPost);
        comment.setAuthor(author);

        // Save comment
        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toDto(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByPostId(Long postId) {
        // Use optimized query to fetch comments with authors
        List<Comment> comments = commentRepository.findByPostIdWithAuthorAndPostOrderByCreatedAtDesc(postId);
        return comments.stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CommentDto getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        return commentMapper.toDto(comment);
    }

    @Transactional
    public CommentDto updateComment(Long commentId, CommentDto commentDto, String authorUsername) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        // Check if the user is the author of the comment
        if (!comment.getAuthor().getUsername().equals(authorUsername)) {
            throw new UnauthorizedException("You can only update your own comments");
        }

        // Update comment
        commentMapper.updateEntityFromDto(comment, commentDto);
        Comment updatedComment = commentRepository.save(comment);

        return commentMapper.toDto(updatedComment);
    }

    @Transactional
    public void deleteComment(Long commentId, String authorUsername) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        // Check if the user is the author of the comment
        if (!comment.getAuthor().getUsername().equals(authorUsername)) {
            throw new UnauthorizedException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }
}
