package com.puspo.scalablekafkaapp.thumbnailservice.kafka;

import com.puspo.scalablekafkaapp.thumbnailservice.dto.UploadEvent;
import com.puspo.scalablekafkaapp.thumbnailservice.service.ThumbnailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j // automatically creates a logger instance (from Simple Logging Facade for Java
       // — SLF4J) and basically removes this boilerplate and gives you a ready-to-use
       // log object.
@RequiredArgsConstructor
public class ThumbnailKafkaConsumer {

    private final ThumbnailService thumbnailService;

    @KafkaListener(topics = "upload-event", containerFactory = "kafkaListenerContainerFactory")
    public void consume(UploadEvent event) {
        log.info("Message received -> metadataId={}, storageName={}", event.getMetadataId(), event.getStorageName());
        try {
            thumbnailService.processThumbnail(event);
        } catch (Exception e) {
            log.error("Error generating thumbnail for event {}", event, e);
        }
    }
}
