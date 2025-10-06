package com.puspo.scalablekafkaapp.kafkaminionginx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Event published when an upload is confirmed.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadEvent {
    private Long metadataId;      // primary key from file_metadata
    private String storageName;   // object key inside bucket
    private String originalName;
    private String contentType;
    private long size;
}
