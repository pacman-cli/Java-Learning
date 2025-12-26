package com.puspo.scalablekafkaapp.videoshare.service.media;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.puspo.scalablekafkaapp.videoshare.exception.CloudinaryException;
import com.puspo.scalablekafkaapp.videoshare.exception.FileValidationException;
import com.puspo.scalablekafkaapp.videoshare.model.Image;
import com.puspo.scalablekafkaapp.videoshare.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final Cloudinary cloudinary;
    private final ImageRepository imageRepository;
    private final FileValidationService fileValidationService;
    private final MetricsService metricsService;

    public Image uploadImage(MultipartFile file) {
        long startTime = System.currentTimeMillis(); // getting the start time of the image upload
        log.info("Starting image upload: filename={}, size={}", file.getOriginalFilename(), file.getSize());

        try {
            // Validate file
            fileValidationService.validateImageFile(file);

            // Upload to Cloudinary
            @SuppressWarnings("unchecked") // suppressing the unchecked warning is basically because we are not using
                                           // the format of the image
            Map<String, Object> uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());

            log.info("Image uploaded to Cloudinary successfully: publicId={}",
                    uploadResult.get("public_id"));

            // Extract metadata
            String publicId = uploadResult.get("public_id").toString();
            String secureUrl = uploadResult.get("secure_url").toString();
            Integer width = (Integer) uploadResult.get("width");
            Integer height = (Integer) uploadResult.get("height");
            // String format = uploadResult.get("format").toString(); // Not used currently

            // Build image entity
            Image image = Image.builder()
                    .name(file.getOriginalFilename())
                    .url(secureUrl)
                    .publicId(publicId)
                    .fileType(fileValidationService.getFileType(file.getContentType()))
                    .mimeType(file.getContentType())
                    .fileSize(file.getSize())
                    .width(width)
                    .height(height)
                    .isProcessed(true)
                    .downloadCount(0L)
                    .build();

            // Save to database
            Image savedImage = imageRepository.save(image);

            // Record metrics
            metricsService.recordImageUpload();
            long processingTime = System.currentTimeMillis() - startTime;
            metricsService.recordProcessingTime("image_upload", processingTime);

            log.info("Image saved successfully: id={}, processingTime={}ms",
                    savedImage.getId(), processingTime);

            return savedImage;

        } catch (IOException e) {
            log.error("Failed to upload image to Cloudinary: {}", e.getMessage());
            metricsService.recordError("image_upload", "cloudinary_error");
            throw new CloudinaryException("Failed to upload image to cloud storage", e);
        } catch (FileValidationException e) {
            log.error("Image validation failed: {}", e.getMessage());
            metricsService.recordError("image_upload", "validation_error");
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during image upload: {}", e.getMessage(), e);
            metricsService.recordError("image_upload", "unexpected_error");
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Cacheable(value = "images", key = "#id")
    public Optional<Image> getImageById(Long id) {
        log.debug("Fetching image by id: {}", id);
        return imageRepository.findById(id);
    }

    public List<Image> getAllImages() {
        log.info("Fetching all images");
        List<Image> images = imageRepository.findAll();
        log.info("Retrieved {} images", images.size());
        return images;
    }

    public String deleteImage(String publicId) {
        log.info("Starting image deletion: publicId={}", publicId);
        long startTime = System.currentTimeMillis();

        try {
            // Delete from Cloudinary
            @SuppressWarnings("unchecked")
            Map<String, Object> result = cloudinary.uploader()
                    .destroy(publicId, ObjectUtils.emptyMap());

            log.info("Image deleted from Cloudinary: result={}", result);

            // Delete from database
            Optional<Image> imageOpt = imageRepository.findByPublicId(publicId);
            if (imageOpt.isPresent()) {
                imageRepository.delete(imageOpt.get());
                log.info("Image deleted from database: id={}", imageOpt.get().getId());
            } else {
                log.warn("Image not found in database: publicId={}", publicId);
            }

            // Record metrics
            metricsService.recordImageDeletion();
            long processingTime = System.currentTimeMillis() - startTime;
            metricsService.recordProcessingTime("image_delete", processingTime);

            return "Image deleted successfully!";

        } catch (IOException e) {
            log.error("Failed to delete image from Cloudinary: {}", e.getMessage());
            metricsService.recordError("image_delete", "cloudinary_error");
            throw new CloudinaryException("Failed to delete image from cloud storage", e);
        } catch (Exception e) {
            log.error("Unexpected error during image deletion: {}", e.getMessage(), e);
            metricsService.recordError("image_delete", "unexpected_error");
            throw new RuntimeException("Failed to delete image", e);
        }
    }

    public void incrementDownloadCount(String publicId) {
        log.debug("Incrementing download count for image: publicId={}", publicId);
        imageRepository.findByPublicId(publicId)
                .ifPresent(image -> {
                    image.setDownloadCount(image.getDownloadCount() + 1);
                    imageRepository.save(image);
                    metricsService.recordImageDownload();
                    log.debug("Download count incremented for image: id={}, count={}",
                            image.getId(), image.getDownloadCount());
                });
    }

    public List<Image> searchImages(String searchTerm) {
        log.info("Searching images with term: {}", searchTerm);
        return imageRepository.findByNameContainingIgnoreCase(searchTerm);
    }

    public List<Image> getImagesByType(String fileType) {
        log.info("Fetching images by type: {}", fileType);
        return imageRepository.findByFileType(fileType);
    }
}