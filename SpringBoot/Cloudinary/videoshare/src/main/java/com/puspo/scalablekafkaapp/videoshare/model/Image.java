package com.puspo.scalablekafkaapp.videoshare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "images", indexes = { // this is the table name and the indexes for the image table
        @Index(name = "idx_public_id", columnList = "publicId"), // this is the index for the publicId column which is unique and used to identify the image
        @Index(name = "idx_upload_date", columnList = "createdAt"), // this is the index for the createdAt column which is used to sort the images by upload date
        @Index(name = "idx_file_type", columnList = "fileType") // this is the index for the fileType column which is used to sort the images by file type
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(nullable = false, unique = true)
    private String publicId;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "tags", length = 1000)
    private String tags;

    @Column(name = "description", length = 2000)
    private String description;

    @CreationTimestamp // this is the timestamp when the image was created
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_processed")
    @Builder.Default // this is the default value for the isProcessed field
    private Boolean isProcessed = false;

    @Column(name = "download_count")
    @Builder.Default
    private Long downloadCount = 0L;
}