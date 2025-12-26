package com.puspo.scalablekafkaapp.videoshare.controller.media;

import com.puspo.scalablekafkaapp.videoshare.model.Video;
import com.puspo.scalablekafkaapp.videoshare.service.media.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
@Tag(name = "Video API", description = "Upload and manage videos")
public class VideoController {

    private final VideoService videoShare;

    @Operation(summary = "Upload a video file", description = "Upload a video to Cloudinary and store it in the database.", requestBody = @RequestBody(content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = VideoUploadSchema.class))))
    @ApiResponse(responseCode = "200", description = "Video uploaded successfully")
    @ApiResponse(responseCode = "400", description = "Invalid video file")
    @ApiResponse(responseCode = "500", description = "Upload failed")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            Video video = videoShare.uploadVideo(file);
            return ResponseEntity.ok(video);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }

    @Operation(summary = "Get all videos", description = "Retrieve all uploaded videos with URLs from MySQL")
    @GetMapping
    public ResponseEntity<Iterable<Video>> getAllVideos() {
        return ResponseEntity.ok(videoShare.getAllVideos());
    }

    @Operation(summary = "Delete video", description = "Delete a video from Cloudinary and MySQL database")
    @DeleteMapping("/{publicId}")
    public ResponseEntity<String> deleteVideo(
            @Parameter(description = "Public ID of the video to delete", required = true) @PathVariable String publicId)
            throws IOException {
        String result = videoShare.deleteVideo(publicId);
        return ResponseEntity.ok(result);
    }

    // 🔹 Inner static class just for Swagger documentation
    static class VideoUploadSchema {
        @Schema(type = "string", format = "binary", description = "Video file to upload")
        public MultipartFile file;
    }
}
