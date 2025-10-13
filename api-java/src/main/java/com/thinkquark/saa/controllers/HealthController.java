package com.thinkquark.saa.controllers;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> status = Map.of(
            "status", "ok",
            "service", "Sensor Anomaly Assistant API",
            "timestamp", Instant.now()
        );

        return ResponseEntity.ok(status);
    }
}
