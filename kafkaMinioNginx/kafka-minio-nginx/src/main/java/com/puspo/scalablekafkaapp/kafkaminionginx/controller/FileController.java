package com.puspo.scalablekafkaapp.kafkaminionginx.controller;

import com.puspo.scalablekafkaapp.kafkaminionginx.storage.ObjectStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final ObjectStorageService objectStorageService;

    //    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a file")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public String upload(
            @Parameter(description = "File to upload", required = true) @RequestParam("file") MultipartFile file
    ) throws Exception {
        String filename = objectStorageService.upload(file);
        return "Uploaded file successfully! -> filename =" + filename;
    }

    @Operation(summary = "Download a file by simple or full filename")
    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable String filename) throws Exception {
        //try direct match first
        byte[] data;
        try {
            data = objectStorageService.download(filename);
        } catch (Exception e) {
            //try fuzzy search if not found
            Optional<String> foundName = objectStorageService.findFileByBaseName(filename);
            if (foundName.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            data = objectStorageService.download(foundName.get()); //download file by base name
            filename = foundName.get(); //update filename if found
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }

    /**
     * Returns a presigned GET URL (expirySeconds seconds).
     */
    @GetMapping("/presigned-url/{filename}")
    public String getPresignedUrl(
            @PathVariable String filename,
            @RequestParam(defaultValue = "3600") int expirySeconds // //default 1 hour
    ) throws Exception { //expiry
        return objectStorageService.getPresignedUrl(filename, expirySeconds);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<String> handleMultipartError(MultipartException e) {
        return ResponseEntity.badRequest().body("Invalid multipart request: " + e.getMessage());
    }
}
