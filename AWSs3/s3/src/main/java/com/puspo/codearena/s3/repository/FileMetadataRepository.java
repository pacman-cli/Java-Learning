package com.puspo.codearena.s3.repository;

import com.puspo.codearena.s3.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata,Long> {
    Optional<FileMetadata> findByS3Key(String s3Key);
}
