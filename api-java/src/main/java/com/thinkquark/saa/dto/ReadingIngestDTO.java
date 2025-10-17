package com.thinkquark.saa.dto;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReadingIngestDTO(
    @NotNull UUID deviceId,
    @NotNull Instant ts,
    @NotNull Double value,
    @Min(0) @Max(3) Integer severity,
    String reason
) { }
