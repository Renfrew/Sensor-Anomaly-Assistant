package com.thinkquark.saa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDeviceRequest(
    @NotBlank @Size(min = 2, max = 128) String name,
    @Size(max = 1024) String description
) { }
