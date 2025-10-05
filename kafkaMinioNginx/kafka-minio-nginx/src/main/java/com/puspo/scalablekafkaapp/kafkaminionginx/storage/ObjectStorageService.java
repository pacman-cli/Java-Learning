package com.puspo.scalablekafkaapp.kafkaminionginx.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ObjectStorageService {
    String upload(MultipartFile file) throws Exception;

    byte[] download(String filename) throws Exception;

    /**
     * Returns a presigned GET URL (expirySeconds seconds).
     */
    String getPresignedUrl(String filename, int expirySeconds) throws Exception;

    void delete(String filename) throws Exception;

    Optional<String> findFileByBaseName(String baseName) throws Exception;

}
