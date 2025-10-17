package com.thinkquark.saa.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thinkquark.saa.dto.ReadingIngestDTO;
import com.thinkquark.saa.dto.ReadingPageDTO;
import com.thinkquark.saa.services.ReadingService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/readings")
public class ReadingController {
    private final ReadingService svc;

    public ReadingController(ReadingService svc) {
        this.svc = svc;
    }

    @GetMapping
    public ReadingPageDTO list(
            @RequestParam UUID deviceId,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to,
            @RequestParam(required = false) Integer severity,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "100") int limit) {
        return svc.list(deviceId, from, to, severity, cursor, limit);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void ingestJson(@Validated @RequestBody List<ReadingIngestDTO> payload) {
        svc.ingest(payload);
    }
    
}
