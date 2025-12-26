package com.puspo.codearena.s3.service;

import com.puspo.codearena.s3.entity.FileMetadata;
import com.puspo.codearena.s3.repository.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class IS3Service implements S3Service {
    private final String bucket;
    private final S3Client s3Client;
    private final FileMetadataRepository fileMetadataRepository;

    public IS3Service(@Value("${aws.s3.bucket}") String bucket, S3Client s3Client, FileMetadataRepository fileMetadataRepository) {
        this.bucket = bucket;
        this.s3Client = s3Client;
        this.fileMetadataRepository = fileMetadataRepository;
    }

    @Override
    public FileMetadata uploadFile(MultipartFile file) throws IOException {
        String key = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        FileMetadata fileMetadata = FileMetadata.builder()
                .filename(file.getOriginalFilename())
                .s3Key(key)
                .size(file.getSize())
                .contentType(file.getContentType())
                .uploadedAt(Instant.now())
                .build();

        return fileMetadataRepository.save(fileMetadata);
    }

    @Override
    public byte[] downloadFile(String s3Key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();
        return s3Client.getObjectAsBytes(getObjectRequest).asByteArray();
    }

    @Override
    public void deleteFile(String s3Key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
        fileMetadataRepository.findByS3Key(s3Key)
                .ifPresent(fileMetadataRepository::delete);

    }

    @Override
    public List<FileMetadata> listAllFiles() {
        return fileMetadataRepository.findAll();
    }

    @Override
    public FileMetadata getMeta(Long id) {
        return fileMetadataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with id: " + id));
    }
}
