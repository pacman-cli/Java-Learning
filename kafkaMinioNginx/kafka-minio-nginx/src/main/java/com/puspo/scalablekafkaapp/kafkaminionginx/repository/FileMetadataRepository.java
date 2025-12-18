package com.puspo.scalablekafkaapp.kafkaminionginx.repository;

import com.puspo.scalablekafkaapp.kafkaminionginx.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
}
