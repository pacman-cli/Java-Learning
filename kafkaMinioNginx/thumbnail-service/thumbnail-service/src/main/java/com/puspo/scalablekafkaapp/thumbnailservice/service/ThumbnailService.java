package com.puspo.scalablekafkaapp.thumbnailservice.service;

import com.puspo.scalablekafkaapp.thumbnailservice.dto.UploadEvent;
import com.puspo.scalablekafkaapp.thumbnailservice.entity.ThumbnailMetadata;
import com.puspo.scalablekafkaapp.thumbnailservice.repository.ThumbnailMetadataRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ThumbnailService {
        private final ThumbnailMetadataRepository thumbnailMetadataRepository;
        // //✅ Convert between Java objects and JSON (both directions).
        // //🧱 Serialization -> Converts a Java object → JSON string
        // //🔄 Deserialization -> Converts a JSON string → Java object
        // ObjectMapper objectMapper = new ObjectMapper();
        private final MinioClient minioClient;

        @Value("${minio.bucket-name}")
        private String bucketName;
        @Value("${thumbnail.width}")
        private int width;
        @Value("${thumbnail.height}")
        private int height;
        @Value("${callback.file-service-url}")
        private String fileServiceCallback;

        public void processThumbnail(UploadEvent event) throws IOException, ServerException, InsufficientDataException,
                        ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
                        XmlParserException, InternalException {
                String fileName = event.getStorageName();

                // thumbnail metadata set
                ThumbnailMetadata thumbnailMetadata = ThumbnailMetadata.builder()
                                .originalFileName(fileName)
                                .thumbnailFileName("thumbnails/" + fileName)
                                .status("PROCESSING")
                                .build();

                // save into thumbnail repo
                thumbnailMetadataRepository.save(thumbnailMetadata);

                try {
                        // Download the file from the bucket
                        InputStream inputStream = minioClient.getObject(
                                        GetObjectArgs.builder()
                                                        .bucket(bucketName)
                                                        .object(fileName) // The path or name of the file inside that
                                                                          // bucket
                                                        .build());
                        // If the inputStream is a JPEG file:
                        // Java decodes that JPEG bytes → pixel matrix → BufferedImage instance.
                        // ✅ After this line, original = full-size image loaded in memory.

                        BufferedImage original = ImageIO.read(inputStream);
                        BufferedImage thumbnail = Scalr.resize(original, width, height); // Resize the image

                        // Setting the thumbnail name
                        String thumbnailName = "thumbnails/" + fileName;
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); //
                        ImageIO.write(thumbnail, "jpg", outputStream);
                        InputStream thumbnailStream = new ByteArrayInputStream(outputStream.toByteArray());

                        // Upload the thumbnail back to the bucket
                        minioClient.putObject(
                                        PutObjectArgs.builder()
                                                        .bucket(bucketName)
                                                        .object(thumbnailName)
                                                        .stream(thumbnailStream, thumbnailStream.available(), -1)
                                                        .contentType("image/jpeg")
                                                        .build());

                        // setting thumbnail metadata
                        thumbnailMetadata.setThumbnailFileName(thumbnailName);
                        thumbnailMetadata.setStatus("SUCCESS");

                        // save into thumbnail repo
                        thumbnailMetadataRepository.save(thumbnailMetadata);

                        // Notify file-service via REST callback
                        RestTemplate restTemplate = new RestTemplate();
                        ResponseEntity<String> responseEntity = restTemplate.postForEntity(fileServiceCallback,
                                        thumbnailMetadata,
                                        String.class);

                        log.info("✅ Thumbnail created and callback sent: {} | Response: {}",
                                        thumbnailName, responseEntity.getStatusCode());
                } catch (Exception e) {
                        thumbnailMetadata.setStatus("FAILED");
                        thumbnailMetadataRepository.save(thumbnailMetadata);
                        log.error("❌ Thumbnail creation failed for {}", fileName, e);
                }
        }
}