package com.puspo.scalablekafkaapp.demominio.controller;

import com.puspo.scalablekafkaapp.demominio.service.MinioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class MinioController {
    private final MinioService minioService;

//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file){
//        String fileName =minioService.uploadFile(file);
//        return ResponseEntity.ok("File uploaded successfully"+ fileName);
//    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) throws Exception {
        byte[] fileBytes = minioService.downloadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileBytes);
    }
    // Inside FileController
//    @Operation(summary = "Upload file to MinIO")
//    @ApiResponse(responseCode = "200", description = "File uploaded successfully")
//    @PostMapping("/upload")
//    public String uploadFile(
//            @RequestBody(
//                    description = "Select a file to upload",
//                    content = @Content(
//                            schema = @Schema(
//                                    type = "string", format = "binary"
//                            )
//                    )
//            )
//            @RequestParam("file") MultipartFile file
//    ) throws Exception {
//        String fileName = minioService.uploadFile(file);
//        return "Uploaded successfully: " + fileName;
//    }

    @Operation(summary = "Upload file to MinIO")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public String uploadFile(
            @Parameter(description = "File to upload")
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        String fileName = minioService.uploadFile(file);
        return "Uploaded successfully: " + fileName;
    }

//    @PostMapping("/upload")
//    public String uploadFile(
//            @Parameter(description = "File to upload", required = true)
//            @RequestParam("file") MultipartFile file
//    ) throws Exception {
//        return minioService.uploadFile(file);
//    }

}
