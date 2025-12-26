package com.pacman.hospital.common.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.Objects;

@Service
public class LocalStorageService implements StorageService {
    private final Path rootLocation;

    public LocalStorageService(@Value("${storage.local.base-dir:uploads}") String baseDir) {
        this.rootLocation = Paths.get(baseDir).toAbsolutePath().normalize(); // resolve to absolute path
        try {
            // Create directories if they do not exist
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create storage directory", e);
        }
    }

    @Override
    public String store(MultipartFile file, String subfolder) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("File is empty");
        }
        String original = StringUtils.cleanPath(Objects.requireNonNullElse(file.getOriginalFilename(), "")); // original
                                                                                                             // filename
                                                                                                             // (nullable-safe)
        String ext = ""; // extension

        int idx = original.lastIndexOf('.'); // get the last index of dot
        if (idx > 0) { // if dot is found
            ext = original.substring(idx); // get extension
        }

        String fileName = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                .format(LocalDateTime.now())
                + "-" + UUID.randomUUID().toString().substring(0, 8) + ext; // generate unique filename with timestamp
                                                                            // and random UUID

        Path folder = (subfolder == null || subfolder.isBlank()) ? rootLocation : rootLocation.resolve(subfolder); // resolve
                                                                                                                   // subfolder
        Files.createDirectories(folder); // create subfolder if not exists

        Path target = folder.resolve(fileName).normalize(); // resolve target path
        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING); // copy file to target
                                                                                            // location
        } catch (IOException e) {
            throw new IOException("Failed to store file " + fileName, e);
        }
        // return a relative path that can be used by the app to serve the file later
        Path relative = rootLocation.relativize(target);
        return relative.toString().replace('\\', '/');
    }

    @Override
    public Path loadPath(String storedPath) {
        return rootLocation.resolve(storedPath).normalize(); // resolve stored path to absolute path
    }
}
