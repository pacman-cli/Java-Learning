package com.pacman.customersupport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pacman.customersupport.entity.DocumentChunk;

@Repository
public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, Long> {
    // Custom query methods (if needed) can be defined here
    // Simple similarity using Euclidean distance (for demo purposes - not efficient
    // for large datasets)
    @Query(value = """
            SELECT *,
            SQRT(
              POWER(CAST(JSON_EXTRACT(embedding, '$[0]') AS DECIMAL) - :e0, 2) +
              POWER(CAST(JSON_EXTRACT(embedding, '$[1]') AS DECIMAL) - :e1, 2) +
              ... -- You need to expand for all 1536 dimensions 😅
            ) as distance
            FROM document_chunks
            ORDER BY distance ASC
            LIMIT :limit
            """, nativeQuery = true)
    List<DocumentChunk> findTopKByEmbedding(
            @Param("e0") Double e0,
            @Param("e1") Double e1,
            // ... add all 1536 params? → NOT PRACTICAL
            @Param("limit") int limit);

}