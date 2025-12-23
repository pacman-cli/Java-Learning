package com.puspo.codearena.s3.service;

import com.puspo.codearena.s3.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3Service {
    FileMetadata uploadFile(MultipartFile file) throws IOException;

    byte[] downloadFile(String s3Key);

    void deleteFile(String s3Key);

    List<FileMetadata> listAllFiles();

    FileMetadata getMeta(Long id);
}
