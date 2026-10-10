package br.com.fiap.inovaflow_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DebugController {

    private final MongoTemplate mongoTemplate;

    @GetMapping("/debug/db")
    public String bancoAtual() {
        return mongoTemplate.getDb().getName();
    }
}