package com.puspo.scalablekafkaapp.kafkaminionginx.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_metadata")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalName;
    private String storageName;
    private String contentType;
    private long size;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime uploadedAt;

    public enum Status {
        PENDING,
        UPLOADED,
        FAILED
    }
}
