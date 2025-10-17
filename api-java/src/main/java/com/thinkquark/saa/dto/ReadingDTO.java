package com.thinkquark.saa.dto;

import java.time.Instant;
import java.util.UUID;

public record ReadingDTO(
    Long id,
    UUID deviceId,
    Instant ts,
    Double value,
    int severity,
    String reason
) { }
