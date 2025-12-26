package com.puspo.scalablekafkaapp.videoshare.service.media;

import com.puspo.scalablekafkaapp.videoshare.exception.FileValidationException;
import com.puspo.scalablekafkaapp.videoshare.util.constants.MediaConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class FileValidationService {

    // Use constants from MediaConstants class

    public void validateImageFile(MultipartFile file) {
        log.info("Validating image file: {}", file.getOriginalFilename()); // getting the original filename of the file
                                                                           // and logging it

        if (file.isEmpty()) {
            throw new FileValidationException("File is empty");
        }

        String contentType = file.getContentType(); // getting the content type of the file
        if (contentType == null || !MediaConstants.ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new FileValidationException(
                    String.format("Invalid image type: %s. Allowed types: %s",
                            contentType, String.join(", ", MediaConstants.ALLOWED_IMAGE_TYPES)));
        }

        if (file.getSize() > MediaConstants.MAX_IMAGE_SIZE) { // check for size exceed
            throw new FileValidationException(
                    String.format("Image size exceeds limit. Max size: %dMB, Actual size: %.2fMB",
                            MediaConstants.MAX_IMAGE_SIZE / (1024 * 1024),
                            (double) file.getSize() / (1024 * 1024)));
        }

        // Validate file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !hasValidImageExtension(originalFilename)) {
            throw new FileValidationException("Invalid file extension for image");
        }

        log.info("Image validation passed for: {}", file.getOriginalFilename());
    }

    public void validateVideoFile(MultipartFile file) {
        log.info("Validating video file: {}", file.getOriginalFilename());

        if (file.isEmpty()) {
            throw new FileValidationException("File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !MediaConstants.ALLOWED_VIDEO_TYPES.contains(contentType.toLowerCase())) {
            throw new FileValidationException(
                    String.format("Invalid video type: %s. Allowed types: %s",
                            contentType, String.join(", ", MediaConstants.ALLOWED_VIDEO_TYPES)));
        }

        if (file.getSize() > MediaConstants.MAX_VIDEO_SIZE) {
            throw new FileValidationException(
                    String.format("Video size exceeds limit. Max size: %dMB, Actual size: %.2fMB",
                            MediaConstants.MAX_VIDEO_SIZE / (1024 * 1024),
                            (double) file.getSize() / (1024 * 1024)));
        }

        // Validate file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !hasValidVideoExtension(originalFilename)) {
            throw new FileValidationException("Invalid file extension for video");
        }

        log.info("Video validation passed for: {}", file.getOriginalFilename());
    }

    private boolean hasValidImageExtension(String filename) {
        return MediaConstants.IMAGE_EXTENSIONS.stream()
                .anyMatch(filename.toLowerCase()::endsWith);
    }

    private boolean hasValidVideoExtension(String filename) {
        return MediaConstants.VIDEO_EXTENSIONS.stream()
                .anyMatch(filename.toLowerCase()::endsWith);
    }

    public String getFileType(String mimeType) {
        if (mimeType == null)
            return "unknown";

        if (mimeType.startsWith("image/")) {
            return "image";
        } else if (mimeType.startsWith("video/")) {
            return "video";
        }

        return "unknown";
    }
}
