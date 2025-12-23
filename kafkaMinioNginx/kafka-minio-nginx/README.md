## kafka-minio-nginx service

Public API for file upload/download, presigned upload, metadata persistence, and emitting Kafka `UploadEvent`s.

### Features
- Presigned PUT upload to MinIO
- Direct multipart upload (for testing)
- Download by exact or fuzzy filename (latest match)
- Persist `FileMetadata` in MySQL
- Publish `UploadEvent` to Kafka (`upload-event` topic)
- OpenAPI/Swagger docs

### API Endpoints
- POST `/api/files/presign` → `{filename, contentType}` → `{id, uploadUrl, storageName}`
- POST `/api/files/confirm/{id}` → marks as `UPLOADED` and publishes event
- POST `/api/files/upload-direct` (multipart `file`)
- GET `/api/files/download/{filename}` → bytes; supports fuzzy matching for `timestamp-<name>` format
- GET `/api/files/presigned-url/{filename}` → presigned GET URL (download)

### Key Classes
- `controller.FilePresignController`, `controller.FileController`
- `service.FileService`, `storage.impl.MinioStorageService`
- `kafka.service.UploadEventPublisher`, `config.KafkaProducerConfig`
- `entity.FileMetadata`, `repository.FileMetadataRepository`
- `config.MinioConfig`, `config.OpenAPIConfig`

### Configuration
See `src/main/resources/application.properties`:
- MinIO: `minio.url`, `minio.access-key`, `minio.secret-key`, `minio.bucket-name`
- MySQL: datasource URL/user/password
- Kafka: `spring.kafka.bootstrap-servers` and serializers
- Swagger: `springdoc.*`

### Run
```bash
./mvnw spring-boot:run
```

### Manual Test
Presign → PUT to uploadUrl → Confirm → Verify MinIO/Kafka/DB.
```bash
curl -s -X POST http://localhost:8080/api/files/presign \
  -H "Content-Type: application/json" \
  -d '{"filename":"photo.jpg","contentType":"image/jpeg"}'

# PUT file to returned uploadUrl with Content-Type header

curl -s -X POST http://localhost:8080/api/files/confirm/1
```

### Notes & Improvements
- Standardize object naming (prefer `UUID-originalName`).
- On confirm, validate object in MinIO and record `size`.
- Move secrets to env vars; protect callback with shared secret.
- Add retries for MinIO & Kafka outbox pattern if needed.


