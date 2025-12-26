package com.puspo.scalablekafkaapp.videoshare.controller.media;

import com.puspo.scalablekafkaapp.videoshare.model.Image;
import com.puspo.scalablekafkaapp.videoshare.service.media.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Tag(name = "Image API", description = "Endpoints for uploading and retrieving images from Cloudinary")
public class ImageController {

    private final ImageService imageService;
    // TODO: this was normal controller. (without swagger file drag)
    // @PostMapping("/upload")
    // public ResponseEntity<Image> uploadImage(
    // @RequestParam("file") MultipartFile file
    // ) throws IOException {
    // return ResponseEntity.ok(imageService.uploadImage(file));
    // }
    //
    // @GetMapping
    // public ResponseEntity<List<Image>> getAllImages() {
    // return ResponseEntity.ok(imageService.getAllImages());
    // }

    // @Operation(summary = "Upload an image", description = "Upload an image file
    // to Cloudinary and save details in " +
    // "MySQL")
    // @PostMapping("/upload")
    // public ResponseEntity<Image> uploadImage(
    // @RequestParam("file") MultipartFile file
    // ) throws IOException {
    // return ResponseEntity.ok(imageService.uploadImage(file));
    // }

    @Operation(summary = "Get all images", description = "Retrieve all uploaded images with URLs from MySQL")
    @GetMapping
    public ResponseEntity<List<Image>> getAllImages() {
        return ResponseEntity.ok(imageService.getAllImages());
    }

    // single file upload
    @PostMapping(value = "/upload", consumes = "multipart/form-data") // sets choose file button to work.
    @Operation(summary = "Upload image", description = "Upload image file to Cloudinary")
    public ResponseEntity<Image> uploadImage(
            @Parameter(description = "Upload file", required = true) @RequestParam("file") MultipartFile file)
            throws Exception {
        return ResponseEntity.ok(imageService.uploadImage(file));
    }

    @Operation(summary = "Delete image", description = "Delete an image from Cloudinary and MySQL database")
    @DeleteMapping("/{publicId}")
    public ResponseEntity<String> deleteImage(
            @Parameter(description = "Public ID of the image to delete", required = true) @PathVariable String publicId)
            throws IOException {
        String result = imageService.deleteImage(publicId);
        return ResponseEntity.ok(result);
    }

    // Multiple file upload
    // @PostMapping(value = "/upload", consumes = "multipart/form-data") //sets
    // choose file button to work.)
    // @Operation(summary = "Upload multiple images", description = "Upload multiple
    // image files to Cloudinary")
    // public ResponseEntity<List<Image>> uploadMultipleImages(
    // @Parameter(description = "upload multiple images", required = true)
    // @RequestParam("files") List<MultipartFile> files
    // ) throws IOException{
    // List<Image> images = new ArrayList<>();
    // for(MultipartFile file: files){
    // images.add(imageService.uploadImage(file));
    // }
    // return ResponseEntity.ok(images);
    // }
}