package com.puspo.codearena.s3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name ="files")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String filename;         // original filename

    @Column(nullable=false, unique=true)
    private String s3Key;            // key used in S3

    private Long size;

    private String contentType;

    private Instant uploadedAt;

}
