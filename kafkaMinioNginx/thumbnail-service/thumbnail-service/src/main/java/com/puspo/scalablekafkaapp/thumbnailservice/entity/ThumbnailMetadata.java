package com.puspo.scalablekafkaapp.thumbnailservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "thumbnails")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThumbnailMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;
    private String thumbnailFileName;
    private String status; // e.g. SUCCESS, FAILED
}
