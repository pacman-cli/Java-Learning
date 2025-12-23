package com.puspo.scalablekafkaapp.videoshare.util.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ResponseHelper {

    public static <T> ResponseEntity<T> success(T data) {
        return ResponseEntity.ok(data);
    }

    public static <T> ResponseEntity<T> success(T data, HttpStatus status) {
        return ResponseEntity.status(status).body(data);
    }

    public static ResponseEntity<Map<String, Object>> error(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<Map<String, Object>> validationError(Map<String, String> errors) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("message", "Invalid input data");
        response.put("validationErrors", errors);
        return ResponseEntity.badRequest().body(response);
    }
}
