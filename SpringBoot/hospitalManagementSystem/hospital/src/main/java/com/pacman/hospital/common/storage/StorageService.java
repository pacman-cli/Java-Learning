package com.pacman.hospital.common.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface StorageService {

    /**
     * Store the file and return a path or URL string that can be saved in the DB. Throws IOException on failure.
     */
    String store(MultipartFile file, String subfolder) throws IOException;

    /**
     * Resolve a stored file path to a Path on disk (optional helper).
     */
    Path loadPath(String storedPath);

}
