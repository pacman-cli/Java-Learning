package com.puspo.scalablekafkaapp.kafkaminionginx.service;

import com.puspo.scalablekafkaapp.kafkaminionginx.entity.FileMetadata;
import com.puspo.scalablekafkaapp.kafkaminionginx.kafka.service.UploadEventPublisher;
import com.puspo.scalablekafkaapp.kafkaminionginx.repository.FileMetadataRepository;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioClient minioClient;
    private final FileMetadataRepository fileMetadataRepository;
    private final UploadEventPublisher uploadEventPublisher;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public FileMetadata createPresignedUpload(
        String originalName,
        String contentType
    ) throws Exception {
        String storageName = UUID.randomUUID() + "_" + originalName;

        // create presigned url for upload
        String url = minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.PUT)
                .bucket(bucketName)
                .object(storageName)
                .expiry(15 * 60) // 15 minutes
                .build()
        );

        // create file metadata
        FileMetadata metadata = FileMetadata.builder()
            .storageName(storageName)
            .originalName(originalName)
            .contentType(contentType)
            .uploadedAt(LocalDateTime.now())
            .status(FileMetadata.Status.PENDING)
            .build();

        // save to repository
        fileMetadataRepository.save(metadata);
        // temporarily reuse the field to return URL
        metadata.setContentType(url);

        return metadata;
    }

    public FileMetadata confirmUpload(Long id) throws Exception {
        FileMetadata existingFile = fileMetadataRepository
            .findById(id)
            .orElseThrow();
        if (existingFile.getStatus() == FileMetadata.Status.PENDING) {
            existingFile.setStatus(FileMetadata.Status.UPLOADED);
            fileMetadataRepository.save(existingFile);
            // now we can publish to Kafka
            uploadEventPublisher.publish(existingFile);
            return existingFile;
        } else {
            throw new IllegalStateException("File is already uploaded");
        }
    }

    public FileMetadata directUpload(MultipartFile file) throws Exception {
        String originalName = file.getOriginalFilename();
        String contentType = file.getContentType();
        String storageName = java.util.UUID.randomUUID() + "_" + originalName;

        // Upload to MinIO (replace with your MinIO logic)
        minioClient.putObject(
            io.minio.PutObjectArgs.builder()
                .bucket(bucketName)
                .object(storageName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(contentType)
                .build()
        );

        FileMetadata metadata = FileMetadata.builder()
            .storageName(storageName)
            .originalName(originalName)
            .contentType(contentType)
            .size(file.getSize())
            .uploadedAt(java.time.LocalDateTime.now())
            .status(FileMetadata.Status.UPLOADED)
            .build();

        fileMetadataRepository.save(metadata);

        // Optionally publish event
        uploadEventPublisher.publish(metadata);

        return metadata;
    }
}
