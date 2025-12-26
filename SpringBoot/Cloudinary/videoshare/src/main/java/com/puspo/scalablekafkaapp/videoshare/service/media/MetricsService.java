package com.puspo.scalablekafkaapp.videoshare.service.media;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsService {

    private final MeterRegistry meterRegistry;

    public void recordImageUpload() {
        Counter.builder("media.upload")
                .tag("type", "image")
                .register(meterRegistry)
                .increment();
        log.debug("Recorded image upload metric");
    }

    public void recordVideoUpload() {
        Counter.builder("media.upload")
                .tag("type", "video")
                .register(meterRegistry)
                .increment();
        log.debug("Recorded video upload metric");
    }

    public void recordImageDownload() {
        Counter.builder("media.download")
                .tag("type", "image")
                .register(meterRegistry)
                .increment();
        log.debug("Recorded image download metric");
    }

    public void recordVideoDownload() {
        Counter.builder("media.download")
                .tag("type", "video")
                .register(meterRegistry)
                .increment();
        log.debug("Recorded video download metric");
    }

    public void recordImageDeletion() {
        Counter.builder("media.delete")
                .tag("type", "image")
                .register(meterRegistry)
                .increment();
        log.debug("Recorded image deletion metric");
    }

    public void recordVideoDeletion() {
        Counter.builder("media.delete")
                .tag("type", "video")
                .register(meterRegistry)
                .increment();
        log.debug("Recorded video deletion metric");
    }

    public void recordApiCall(String endpoint, String method, int statusCode) {
        Counter.builder("api.calls")
                .tag("endpoint", endpoint)
                .tag("method", method)
                .tag("status", String.valueOf(statusCode))
                .register(meterRegistry)
                .increment();
        log.debug("Recorded API call metric: {} {} - {}", method, endpoint, statusCode);
    }

    public void recordProcessingTime(String operation, long timeMs) {
        Timer.builder("media.processing.time")
                .tag("operation", operation)
                .register(meterRegistry)
                .record(timeMs, java.util.concurrent.TimeUnit.MILLISECONDS);
        log.debug("Recorded processing time: {} - {}ms", operation, timeMs);
    }

    public void recordError(String operation, String errorType) {
        Counter.builder("media.errors")
                .tag("operation", operation)
                .tag("error_type", errorType)
                .register(meterRegistry)
                .increment();
        log.debug("Recorded error metric: {} - {}", operation, errorType);
    }
}
