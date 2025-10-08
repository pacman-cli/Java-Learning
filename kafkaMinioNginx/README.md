## Kafka + MinIO + NGINX Microservices

This repository contains two Spring Boot microservices that implement a file upload and thumbnail-generation pipeline backed by MinIO (S3-compatible) and Kafka.

- `kafka-minio-nginx`: Public API for uploading/downloading files, generating presigned upload URLs, persisting file metadata, and publishing Kafka events after uploads.
- `thumbnail-service`: Kafka consumer that generates image thumbnails from uploaded objects, stores its own metadata, and notifies the file service via HTTP callback.


### High-level Architecture and Data Flow

1) Client requests a presigned URL from the file service.
2) Client uploads the file directly to MinIO using the presigned URL.
3) Client calls the file service to confirm the upload.
4) File service updates metadata and publishes an `UploadEvent` to Kafka.
5) Thumbnail service consumes the event, downloads the original image from MinIO, generates a thumbnail, uploads it back to MinIO under `thumbnails/`, stores thumbnail metadata, and sends a callback to the file service.


### Implementation Overview

#### kafka-minio-nginx (File API + Kafka Producer)
- Controllers
  - `FileController`
    - `POST /api/files/upload` (multipart): Direct upload to MinIO via `ObjectStorageService.upload`.
    - `GET /api/files/download/{filename}`: Download by exact key; if not found, fuzzy match by base name via `findFileByBaseName`.
    - `GET /api/files/presigned-url/{filename}`: Returns presigned GET URL for downloads.
  - `FilePresignController`
    - `POST /api/files/presign`: Returns `{ id, uploadUrl, storageName }` for a presigned PUT upload.
    - `POST /api/files/confirm/{id}`: Marks file metadata as `UPLOADED` and publishes Kafka `UploadEvent`.
    - `POST /api/files/upload-direct` (multipart): Alternative to presign; uploads directly and publishes event.
    - `POST /api/files/thumbnail-ready`: Receives callback from thumbnail service and logs receipt.
- Services
  - `FileService`
    - `createPresignedUpload(originalName, contentType)`: Builds presigned PUT URL, creates `FileMetadata` with `PENDING`.
    - `confirmUpload(id)`: Sets `UPLOADED`, saves, publishes `UploadEvent`.
    - `directUpload(file)`: Streams to MinIO, saves metadata, publishes event.
  - `MinioStorageService` (implements `ObjectStorageService`)
    - Ensures bucket exists at startup; supports upload/download/presigned GET/delete and fuzzy file search.
- Kafka
  - `UploadEventPublisher`: Sends `UploadEvent` to topic `upload-event` using `KafkaTemplate<String, UploadEvent>`.
  - `KafkaProducerConfig`: Configures producer with `JsonSerializer`.
- Persistence
  - `FileMetadata` entity with `Status` (PENDING/UPLOADED/FAILED), names, contentType, size, and timestamps.
  - `FileMetadataRepository` (Spring Data JPA).
- Config
  - `MinioConfig` for `MinioClient` bean; `OpenAPIConfig` for Swagger.
  - `application.properties` for MinIO, MySQL, Kafka, Swagger settings.

#### thumbnail-service (Kafka Consumer + Thumbnail Generation)
- Kafka
  - `ThumbnailKafkaConsumer.consume(String message)`: Logs and calls service. Note: missing `@KafkaListener` and consumer factory is commented out (see improvements).
- Service
  - `ThumbnailService.processThumbnail(String message)`:
    - Parses JSON to extract `storageName`.
    - Saves `ThumbnailMetadata` with status PROCESSING.
    - Builds its own `MinioClient` (not using configured bean), downloads original, resizes with `imgscalr`, uploads thumbnail to `thumbnails/{storageName}`.
    - Updates status SUCCESS; posts callback to file service with `RestTemplate`.
    - On error, marks FAILED.
- Persistence
  - `ThumbnailMetadata` entity and `ThumbnailMetadataRepository` (JPA).
- Config
  - `MinioConfig` bean (not used by service code currently).
  - `KafkaConsumerConfig` is present but fully commented out.
  - `application.properties` includes MinIO, MySQL, Kafka consumer props, thumbnail sizes, and callback URL.


### Gaps, Improvements, and Bug Fixes (Actionable)

1) Wire up Kafka consumer in `thumbnail-service`
- Add `@KafkaListener(topics = "upload-event", groupId = "thumbnail-group")` and enable a listener factory.
- Use `JsonDeserializer<UploadEvent>` so the method consumes `UploadEvent` directly instead of parsing raw JSON.

2) Use injected `MinioClient` in `ThumbnailService`
- Remove inline client creation; inject the `MinioClient` bean from `MinioConfig` for consistency and testability.

3) Presign flow cleanup in file service
- Do not overload `FileMetadata.contentType` to carry presigned URL. Return URL in the DTO only (e.g., `FilePresignResponse.uploadUrl`) without mutating the entity field.

4) Standardize object naming
- Use a single naming convention across all upload paths (e.g., `UUID-originalName`). Update `findFileByBaseName` or deprecate it in favor of explicit keys.

5) Confirm upload validation
- On `confirmUpload`, call MinIO `statObject` to verify existence; populate `size` and set `uploadedAt` upon completion.

6) Security and configuration hygiene
- Move DB/MinIO creds to environment variables and Spring profiles; avoid committing secrets.
- Protect callback endpoint with a shared secret header or mTLS.
- Validate upload `contentType` against an allowlist.

7) Resilience and reliability
- Add retry/backoff for MinIO operations and HTTP callback (e.g., Spring Retry).
- Configure Kafka retry and DLT for consumer failures in thumbnail service.

8) API responses and observability
- Use `ResponseStatusException` for `confirmUpload` 404/409 cases.
- Add structured logs (file id, object key) and basic metrics.

9) Testing
- Add Testcontainers-based integration tests for MinIO, Kafka, and MySQL to validate end-to-end behavior.


### Local Setup

Prerequisites
- Docker (for MinIO, Kafka, MySQL) or equivalent local services.
- JDK 17+ and Maven.

Services & Ports (default)
- File service: `http://localhost:8080`
- Thumbnail service: `http://localhost:8081` (or default Spring port if not changed)
- MinIO: `http://localhost:9000` (access/secret: `minioadmin/minioadmin`)
- Kafka: `localhost:9092` with topic `upload-event`
- MySQL:
  - `jdbc:mysql://localhost:3306/minio_demo` (file service)
  - `jdbc:mysql://localhost:3306/thumbnail_db` (thumbnail service)

Start apps
```bash
# in kafka-minio-nginx/
./mvnw spring-boot:run

# in thumbnail-service/thumbnail-service/
./mvnw spring-boot:run
```


### Manual Testing Guide

Presigned upload flow
1) Request presign (file service):
```bash
curl -s -X POST http://localhost:8080/api/files/presign \
  -H "Content-Type: application/json" \
  -d '{"filename":"photo.jpg","contentType":"image/jpeg"}'
```
Response example:
```json
{ "id": 1, "uploadUrl": "https://minio/...", "storageName": "<UUID>_photo.jpg" }
```

2) Upload file to MinIO using the returned `uploadUrl` (PUT); include content type header:
```bash
curl -s -X PUT "<uploadUrl from step 1>" \
  -H "Content-Type: image/jpeg" \
  --data-binary @./photo.jpg
```

3) Confirm upload (file service):
```bash
curl -s -X POST http://localhost:8080/api/files/confirm/1
```
Expected: metadata updated to `UPLOADED`; Kafka event published.

4) Verify results:
- MinIO: original object exists at `storageName`; after consumer runs, thumbnail exists at `thumbnails/<storageName>`.
- DBs: `minio_demo.file_metadata` row is `UPLOADED`; `thumbnail_db.thumbnails` row is `SUCCESS` with thumbnail name.
- Logs: file service logs event publish and callback receipt; thumbnail service logs thumbnail created.

Direct upload flow (quick test)
```bash
curl -s -X POST http://localhost:8080/api/files/upload-direct \
  -F "file=@./photo.jpg"
```
Expected: metadata returned; Kafka event published; thumbnail created.

Download verification
```bash
curl -OJ http://localhost:8080/api/files/download/<full-or-base-name>
```
Prefer full object key for deterministic downloads.

Negative tests
- Confirm with invalid id → expect 404/409 after improvement.
- Upload non-image → thumbnail service should mark FAILED and not crash.
- Kafka down → confirm should log/persist appropriately; consider retry/outbox in future.


### Configuration Pointers

- File service: `kafka-minio-nginx/src/main/resources/application.properties`
- Thumbnail service: `thumbnail-service/thumbnail-service/src/main/resources/application.properties`
- Helpful docs in `kafka-minio-nginx/HELP.md` and `thumbnail-service/thumbnail-service/HELP.md`.


### Next Steps (Recommended)
- Implement the consumer wiring, MinIO bean usage in thumbnail service, presign cleanup, and naming standardization.
- Add end-to-end tests with Testcontainers.


