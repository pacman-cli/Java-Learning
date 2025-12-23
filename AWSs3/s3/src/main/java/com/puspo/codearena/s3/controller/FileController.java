package com.puspo.codearena.s3.controller;

import com.puspo.codearena.s3.entity.FileMetadata;
import com.puspo.codearena.s3.service.S3Service;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final S3Service s3Service;

    public FileController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileMetadata> upload(
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        FileMetadata meta = s3Service.uploadFile(file);
        return ResponseEntity.ok(meta);
    }

    @GetMapping
    public ResponseEntity<List<FileMetadata>> list() {
        return ResponseEntity.ok(s3Service.listAllFiles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileMetadata> getMeta(@PathVariable Long id) {
        return ResponseEntity.ok(s3Service.getMeta(id));
    }

    @GetMapping("/download/{s3Key}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String s3Key) {
        byte[] data = s3Service.downloadFile(s3Key);
        // Get contentType from metadata if you want exact type
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + s3Key + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(data.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(data));
    }

    @DeleteMapping("/{s3Key}")
    public ResponseEntity<Void> delete(@PathVariable String s3Key) {
        s3Service.deleteFile(s3Key);
        return ResponseEntity.noContent().build();
    }
}
