package com.pacman.blogify.repository;

import com.pacman.blogify.model.BlogPost;
import com.pacman.blogify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    List<BlogPost> findByAuthor(User author);

    // Optimized queries to avoid N+1 problem
    @Query("SELECT p FROM BlogPost p JOIN FETCH p.author ORDER BY p.createdAt DESC")
    List<BlogPost> findAllWithAuthor();

    @Query("SELECT p FROM BlogPost p JOIN FETCH p.author WHERE p.id = :id")
    Optional<BlogPost> findByIdWithAuthor(Long id);

    @Query("SELECT p FROM BlogPost p JOIN FETCH p.author WHERE p.author = :author ORDER BY p.createdAt DESC")
    List<BlogPost> findByAuthorWithAuthor(User author);
}
