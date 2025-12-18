package com.pacman.blogify.repository;

import com.pacman.blogify.model.BlogPost;
import com.pacman.blogify.model.Comment;
import com.pacman.blogify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(BlogPost blogPost);

    List<Comment> findByAuthor(User author);

    List<Comment> findByPostOrderByCreatedAtDesc(BlogPost blogPost);

    List<Comment> findByAuthorOrderByCreatedAtDesc(User author);

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId ORDER BY c.createdAt DESC")
    List<Comment> findByPostIdOrderByCreatedAtDesc(@Param("postId") Long postId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.author JOIN FETCH c.post WHERE c.post.id = :postId ORDER BY c.createdAt DESC")
    List<Comment> findByPostIdWithAuthorAndPostOrderByCreatedAtDesc(@Param("postId") Long postId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    Long countByPostId(@Param("postId") Long postId);
}
