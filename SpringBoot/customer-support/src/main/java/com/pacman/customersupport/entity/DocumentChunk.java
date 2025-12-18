package com.pacman.customersupport.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "document_chunks")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;

    @Column(length = 8192) // Adjust length based on embedding size
    private String embedding; // Store embedding as a JSON string or comma-separated values

    private String sourceDocument;
    
}
