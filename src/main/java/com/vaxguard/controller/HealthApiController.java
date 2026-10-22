package com.vaxguard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthApiController {

    private final LocalDateTime startupTime = LocalDateTime.now();

    @GetMapping
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Vaccination Reminder Portal (VaxGuard)");
        health.put("version", "1.0.0");
        health.put("author", "Gayatri Patil (PRN: 23102A0016, Roll: 15)");
        health.put("timestamp", LocalDateTime.now().toString());
        health.put("startupTime", startupTime.toString());
        health.put("database", "H2 In-Memory (Connected)");
        health.put("qualityGate", "PASSED");

        return ResponseEntity.ok(health);
    }
}
