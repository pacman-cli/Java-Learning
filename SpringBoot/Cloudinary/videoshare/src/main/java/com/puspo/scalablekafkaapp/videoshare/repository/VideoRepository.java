package com.puspo.scalablekafkaapp.videoshare.repository;

import com.puspo.scalablekafkaapp.videoshare.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    Optional<Video> findByPublicId(String publicId);
}
