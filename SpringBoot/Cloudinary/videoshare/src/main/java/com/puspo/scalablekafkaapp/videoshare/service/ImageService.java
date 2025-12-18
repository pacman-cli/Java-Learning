package com.puspo.scalablekafkaapp.videoshare.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.puspo.scalablekafkaapp.videoshare.model.Image;
import com.puspo.scalablekafkaapp.videoshare.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final Cloudinary cloudinary;
    private final ImageRepository imageRepository;

    public Image uploadImage(MultipartFile file) throws Exception {
        // Upload the image to Cloudinary
        Map uploadResult = cloudinary.uploader()
                .upload(
                        file.getBytes(),
                        ObjectUtils.emptyMap()
                );

        // Save the image to the database
        Image image = Image.builder()
                .name(file.getOriginalFilename())
                .url(uploadResult.get("secure_url").toString())
                .publicId(uploadResult.get("public_id").toString())
                .build();

        System.out.println(cloudinary.api().ping(ObjectUtils.emptyMap()));

        return imageRepository.save(image);
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public String deleteImage(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        imageRepository.findByPublicId(publicId).ifPresent(imageRepository::delete); //if found then delete
        return "Deleted successfully!";
    }

}
