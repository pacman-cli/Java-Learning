package com.puspo.scalablekafkaapp.videoshare.util.constants;

import java.util.Arrays;
import java.util.List;

public class MediaConstants {
        // File size limits (in bytes)
        public static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024; // 10MB
        public static final long MAX_VIDEO_SIZE = 500 * 1024 * 1024; // 500MB

        // Allowed image types
        public static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
                        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp", "image/bmp");

        // Allowed video types
        public static final List<String> ALLOWED_VIDEO_TYPES = Arrays.asList(
                        "video/mp4", "video/avi", "video/mov", "video/wmv", "video/flv", "video/webm", "video/mkv");

        // Image extensions
        public static final List<String> IMAGE_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp",
                        ".bmp");

        // Video extensions
        public static final List<String> VIDEO_EXTENSIONS = Arrays.asList(".mp4", ".avi", ".mov", ".wmv", ".flv",
                        ".webm", ".mkv");
}
