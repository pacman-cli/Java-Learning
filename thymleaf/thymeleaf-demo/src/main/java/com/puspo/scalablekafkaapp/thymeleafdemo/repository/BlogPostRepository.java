package com.puspo.scalablekafkaapp.thymeleafdemo.repository;


import com.puspo.scalablekafkaapp.thymeleafdemo.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    // Optionally: custom query to order by newest first
    List<BlogPost> findAllByOrderByCreatedAtDesc();
}

