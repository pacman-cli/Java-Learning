package com.puspo.scalablekafkaapp.kafkaminionginx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for client request to generate a presigned upload URL.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilePresignRequest {
    private String filename;
    private String contentType;
}