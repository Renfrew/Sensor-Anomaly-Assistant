package com.thinkquark.saa.dto;

import java.time.Instant;
import java.util.UUID;

public record DeviceDTO(
    UUID id,
    String name,
    String description,
    Instant createdAt
) {}
