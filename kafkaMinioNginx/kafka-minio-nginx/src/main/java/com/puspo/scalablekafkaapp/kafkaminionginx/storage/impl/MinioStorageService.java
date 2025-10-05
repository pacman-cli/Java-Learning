package com.puspo.scalablekafkaapp.kafkaminionginx.storage.impl;

import com.puspo.scalablekafkaapp.kafkaminionginx.storage.ObjectStorageService;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;

@Service
public class MinioStorageService implements ObjectStorageService {
    private final MinioClient minioClient;
    private final String bucketName;

    public MinioStorageService(MinioClient minioClient, @Value("${minio.bucket-name}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
        verifyBucketExist();
    }

    //verify bucket exist
    private void verifyBucketExist() {
        try {
            boolean isFound = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
            if (!isFound) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to ensure bucket exists: " + bucketName, e);
        }
    }

    @Override
    public String upload(MultipartFile file) throws Exception {
        String filename = System.currentTimeMillis() + "-" + file.getOriginalFilename();

        try (InputStream inputStream = file.getInputStream()) {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .stream(inputStream, file.getSize(), -1) //this gives progress
                    .contentType(file.getContentType())
                    .build();

            minioClient.putObject(putObjectArgs);
        }
        return filename;
    }

    @Override
    public byte[] download(String filename) throws Exception {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .build();
        try (InputStream inputStream = minioClient.getObject(getObjectArgs)) {
            return inputStream.readAllBytes();
        }
    }

    @Override
    public String getPresignedUrl(String filename, int expirySeconds) throws Exception {
        GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(filename)
                .expiry(expirySeconds)
                .build();
        return minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
    }

    @Override
    public void delete(String filename) throws Exception {
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .build();
        minioClient.removeObject(removeObjectArgs);
    }

    @Override
    public Optional<String> findFileByBaseName(String baseName) throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .build()
        );
        String lowerbase = baseName.toLowerCase();
        String latestMatch = null;
        long latestTimestamp = -1;
        for (Result<Item> result : results) {
            Item item = result.get();
            String objectName = item.objectName();

            // Example: 1759696929167-diabetes.csv
            String lowerName = objectName.toLowerCase();

            // Check if name contains "diabetes" after the '-'
            if (lowerName.contains("-" + lowerbase)) {
                try {
                    //parse the timestamp(before) from the filename
                    String prefix = objectName.split("-", 2)[0];
                    long timestamp = Long.parseLong(prefix); //pasing to long
                    if (timestamp > latestTimestamp) {
                        latestTimestamp = timestamp;
                        latestMatch = objectName;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return Optional.ofNullable(latestMatch);
    }
}
