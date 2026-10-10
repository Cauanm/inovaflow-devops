package br.com.fiap.inovaflow_backend.controller;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class HealthController {

    private final MongoTemplate mongoTemplate;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("application", "InovaFlow Backend");

        try {
            mongoTemplate.getDb()
                    .runCommand(new Document("ping", 1));

            response.put("status", "UP");
            response.put("mongodb", "UP");
            response.put("database", mongoTemplate.getDb().getName());

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            response.put("status", "DOWN");
            response.put("mongodb", "DOWN");

            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(response);
        }
    }
}