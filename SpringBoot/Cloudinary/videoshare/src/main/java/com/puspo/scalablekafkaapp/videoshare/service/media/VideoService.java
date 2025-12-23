package com.puspo.scalablekafkaapp.videoshare.service.media;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import com.puspo.scalablekafkaapp.videoshare.model.Video;
import com.puspo.scalablekafkaapp.videoshare.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final Cloudinary cloudinary;
    private final VideoRepository videoRepository;

    public Video uploadVideo(MultipartFile file) throws IOException {
        try {
            // Validate file type
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File is empty");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("video/")) {
                throw new IllegalArgumentException("File must be a video. Received: " + contentType);
            }

            // Validate file size (500MB limit)
            long maxSize = 500 * 1024 * 1024; // 500MB
            if (file.getSize() > maxSize) {
                throw new IllegalArgumentException(
                        "File size must be less than 500MB. Received: " + (file.getSize() / (1024 * 1024)) + "MB");
            }

            // Upload the video to Cloudinary
            @SuppressWarnings("unchecked") // suppressing the unchecked warning is basically because we are not using
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("resource_type", "video") // import for video upload
            );

            // Save the video to the database
            Video video = Video.builder()
                    .name(file.getOriginalFilename())
                    .url(uploadResult.get("secure_url").toString())
                    .publicId(uploadResult.get("public_id").toString())
                    .build();

            return videoRepository.save(video);
        } catch (Exception e) {
            throw new IOException("Failed to upload video: " + e.getMessage(), e);
        }
    }

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    public String deleteVideo(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        videoRepository.findByPublicId(publicId).ifPresent(videoRepository::delete); // if found then delete
        return "Deleted successfully!";
    }
}
