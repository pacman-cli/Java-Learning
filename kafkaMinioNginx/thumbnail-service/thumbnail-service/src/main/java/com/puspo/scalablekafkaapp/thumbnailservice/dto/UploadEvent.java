package com.puspo.scalablekafkaapp.thumbnailservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadEvent {
    private Long metadataId;
    private String storageName;
    private String originalName;
    private String contentType;
    private long size;
}
