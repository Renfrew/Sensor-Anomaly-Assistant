package com.thinkquark.saa.controllers;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

import com.thinkquark.saa.dto.CreateDeviceRequest;
import com.thinkquark.saa.dto.DeviceDTO;
import com.thinkquark.saa.services.DeviceService;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    private final DeviceService svc;
    public DeviceController(DeviceService svc) {
        this.svc = svc;
    }

    @GetMapping
    public Page<DeviceDTO> list(
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int limit
    ) {
        return svc.list(search, page, limit);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceDTO create(@Validated @RequestBody CreateDeviceRequest body) {
        return svc.create(body);
    }
}
