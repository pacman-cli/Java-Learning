package com.puspo.scalablekafkaapp.kafkaminionginx.controller;

import com.puspo.scalablekafkaapp.kafkaminionginx.dto.FilePresignRequest;
import com.puspo.scalablekafkaapp.kafkaminionginx.dto.FilePresignResponse;
import com.puspo.scalablekafkaapp.kafkaminionginx.entity.FileMetadata;
import com.puspo.scalablekafkaapp.kafkaminionginx.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FilePresignController {
    private final FileService fileService;

    @Operation(summary = "Generate a presigned URL for direct upload to MinIO")
    @PostMapping("/presign")
    public FilePresignResponse presign(
            @RequestBody FilePresignRequest request
    ) throws Exception {
        FileMetadata fileMetadata = fileService.createPresignedUpload(request.getFilename(), request.getContentType());
        return new FilePresignResponse(fileMetadata.getId(), fileMetadata.getContentType(),
                fileMetadata.getStorageName());
    }

    @Operation(summary = "Confirm upload after client finished uploading to MinIO")
    @PostMapping("/confirm/{id}")
    public FileMetadata confirm(@PathVariable Long id) throws Exception {
        return fileService.confirmUpload(id);
    }
}
