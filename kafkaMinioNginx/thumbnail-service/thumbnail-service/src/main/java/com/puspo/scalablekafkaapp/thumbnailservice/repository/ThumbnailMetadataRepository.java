package com.puspo.scalablekafkaapp.thumbnailservice.repository;

import com.puspo.scalablekafkaapp.thumbnailservice.entity.ThumbnailMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThumbnailMetadataRepository extends JpaRepository<ThumbnailMetadata,Long>{
}