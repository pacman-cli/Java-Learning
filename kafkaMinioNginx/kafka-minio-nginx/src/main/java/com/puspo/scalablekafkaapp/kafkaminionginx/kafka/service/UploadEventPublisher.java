package com.puspo.scalablekafkaapp.kafkaminionginx.kafka.service;

import com.puspo.scalablekafkaapp.kafkaminionginx.dto.UploadEvent;
import com.puspo.scalablekafkaapp.kafkaminionginx.entity.FileMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UploadEventPublisher {

    public final KafkaTemplate<String, UploadEvent> kafkaTemplate;
    public final String topic = "upload-event";

    /**
     * Publishes an upload event to the Kafka topic.
     *
     * @param fileMetadata the metadata of the uploaded file to be published as an event
     */
    public void publish(FileMetadata fileMetadata) {
        // Create an UploadEvent object from the provided file metadata
        UploadEvent uploadEvent = new UploadEvent(
            fileMetadata.getId(),
            fileMetadata.getStorageName(),
            fileMetadata.getOriginalName(),
            fileMetadata.getContentType(),
            fileMetadata.getSize()
        );
        // Send the UploadEvent to the configured Kafka topic
        kafkaTemplate.send(topic, uploadEvent);
    }
}
