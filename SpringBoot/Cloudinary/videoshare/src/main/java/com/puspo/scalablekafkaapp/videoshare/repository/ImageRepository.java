package com.puspo.scalablekafkaapp.videoshare.repository;

import com.puspo.scalablekafkaapp.videoshare.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByPublicId(String publicId);

    List<Image> findByNameContainingIgnoreCase(String name);

    List<Image> findByFileType(String fileType);

    List<Image> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Image> findByFileSizeLessThan(Long maxSize);

    List<Image> findByFileSizeGreaterThan(Long minSize);

    List<Image> findByIsProcessed(Boolean isProcessed);

    @Query("SELECT i FROM Image i WHERE " +
            "(:searchTerm IS NULL OR " +
            "LOWER(i.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(i.tags) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Image> searchImages(@Param("searchTerm") String searchTerm); // this is the query to search the images by name
                                                                      // and tags which is case insensitive and contains
                                                                      // the search term

    @Query("SELECT i FROM Image i ORDER BY i.downloadCount DESC")
    List<Image> findTopDownloadedImages();

    @Query("SELECT i FROM Image i ORDER BY i.createdAt DESC")
    List<Image> findRecentImages();

    @Query("SELECT COUNT(i) FROM Image i WHERE i.fileType = :fileType")
    Long countByFileType(@Param("fileType") String fileType);

    @Query("SELECT SUM(i.fileSize) FROM Image i")
    Long getTotalStorageUsed();
}