package com.puspo.scalablekafkaapp.videoshare.repository;


import com.fasterxml.jackson.annotation.JacksonInject;
import com.puspo.scalablekafkaapp.videoshare.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByPublicId(String publicId);
}

