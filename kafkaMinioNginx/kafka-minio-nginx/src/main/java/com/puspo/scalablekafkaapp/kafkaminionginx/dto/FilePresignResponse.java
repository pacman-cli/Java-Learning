package com.puspo.scalablekafkaapp.kafkaminionginx.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for response containing presigned upload URL and metadata.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilePresignResponse {
    private Long id;
    private String uploadUrl;
    private String storageName;
}