package com.puspo.scalablekafkaapp.kafkaminionginx.controller;
//
//import com.puspo.scalablekafkaapp.kafkaminionginx.dto.FilePresignRequest;
//import com.puspo.scalablekafkaapp.kafkaminionginx.dto.FilePresignResponse;
//import com.puspo.scalablekafkaapp.kafkaminionginx.entity.FileMetadata;
//import com.puspo.scalablekafkaapp.kafkaminionginx.service.FileService;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/files")
//@RequiredArgsConstructor
//public class FilePresignController {
//    private final FileService fileService;
//
//    @Operation(summary = "Generate a presigned URL for direct upload to MinIO")
//    @PostMapping("/presign")
//    public FilePresignResponse presign(
//            @RequestBody FilePresignRequest request
//    ) throws Exception {
//        FileMetadata fileMetadata = fileService.createPresignedUpload(request.getFilename(), request.getContentType());
//        return new FilePresignResponse(fileMetadata.getId(), fileMetadata.getContentType(),
//                fileMetadata.getStorageName());
//    }
//
//    @Operation(summary = "Confirm upload after client finished uploading to MinIO")
//    @PostMapping("/confirm/{id}")
//    public FileMetadata confirm(@PathVariable Long id) throws Exception {
//        return fileService.confirmUpload(id);
//    }
//}


import com.puspo.scalablekafkaapp.kafkaminionginx.dto.FilePresignRequest;
import com.puspo.scalablekafkaapp.kafkaminionginx.dto.FilePresignResponse;
import com.puspo.scalablekafkaapp.kafkaminionginx.entity.FileMetadata;
import com.puspo.scalablekafkaapp.kafkaminionginx.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FilePresignController {

    private final FileService fileService;

    @Operation(
            summary = "Generate a presigned upload URL",
            description = "Use this endpoint to get a presigned URL for uploading directly to MinIO.",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(value = """
                                            {
                                              "filename": "photo.jpg",
                                              "contentType": "image/jpeg"
                                            }
                                            """)
                            }
                    )
            )
    )
    @ApiResponse(responseCode = "200", description = "Presigned URL generated successfully")
    @PostMapping("/presign")
    public FilePresignResponse presign(@org.springframework.web.bind.annotation.RequestBody FilePresignRequest req) throws Exception {
        FileMetadata meta = fileService.createPresignedUpload(req.getFilename(), req.getContentType());
        return new FilePresignResponse(meta.getId(), meta.getContentType(), meta.getStorageName());
    }

    @Operation(summary = "Confirm file upload and trigger Kafka event")
    @PostMapping("/confirm/{id}")
    public FileMetadata confirm(@PathVariable Long id) throws Exception {
        return fileService.confirmUpload(id);
    }

    @Operation(
            summary = "Upload file directly through API (optional)",
            description = "This endpoint uploads a file directly to MinIO (for testing without presigned URLs)."
    )
    @PostMapping(value = "/upload-direct", consumes = "multipart/form-data")
    public FileMetadata upload(@RequestPart("file") MultipartFile file) throws Exception {
        return fileService.directUpload(file);
    }

    @PostMapping("/thumbnail-ready")
    public ResponseEntity<String> handleThumbnailCallback(@RequestBody Map<String, Object> data) {
        log.info("✅ Thumbnail ready callback received: {}", data);
        return ResponseEntity.ok("Received thumbnail callback successfully");
    }

}
