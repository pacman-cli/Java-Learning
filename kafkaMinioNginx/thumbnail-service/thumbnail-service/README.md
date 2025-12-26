## thumbnail-service

Consumes Kafka `upload-event` messages, generates thumbnails from images in MinIO, stores metadata, uploads thumbnails back to MinIO, and sends a callback to the file service.

### Features
- `@KafkaListener` consumes `UploadEvent` (JSON) via `JsonDeserializer`
- Downloads original object from MinIO
- Resizes using `imgscalr` to `thumbnail.width` x `thumbnail.height`
- Uploads thumbnail to `thumbnails/{storageName}`
- Persists `ThumbnailMetadata` (status: PROCESSING/SUCCESS/FAILED)
- HTTP callback to file service after success

### Key Classes
- `kafka.ThumbnailKafkaConsumer` (listener)
- `service.ThumbnailService` (thumbnail pipeline)
- `dto.UploadEvent` (Kafka message type)
- `entity.ThumbnailMetadata`, `repository.ThumbnailMetadataRepository`
- `config.KafkaConsumerConfig`, `config.MinioConfig`

### Configuration
See `src/main/resources/application.properties`:
- MinIO: `minio.url`, `minio.access-key`, `minio.secret-key`, `minio.bucket-name`
- Kafka: `spring.kafka.bootstrap-servers`, `spring.kafka.consumer.group-id`
- Thumbnail: `thumbnail.width`, `thumbnail.height`
- Callback: `callback.file-service-url`

### Run
```bash
./mvnw spring-boot:run
```

### Manual Test (end-to-end)
Triggered by file-service after confirm:
1) In file-service, presign → upload → confirm (see file-service README).
2) This service will consume the event and create thumbnail.
3) Verify MinIO at `thumbnails/{storageName}` and DB row in `thumbnails` table.

### Notes & Improvements
- Retries/backoff for MinIO operations and callback
- Dead-letter topic and retry for Kafka consumer failures
- Validate input content types and guard against non-image data


