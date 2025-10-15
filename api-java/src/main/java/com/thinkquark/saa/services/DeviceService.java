package com.thinkquark.saa.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.thinkquark.saa.repos.DeviceRepo;
import com.thinkquark.saa.dto.CreateDeviceRequest;
import com.thinkquark.saa.dto.DeviceDTO;
import com.thinkquark.saa.entities.Device;

@Service
public class DeviceService {
	private final DeviceRepo repo;

    public DeviceService(DeviceRepo repo) {
        this.repo = repo;
    }

    public Page<DeviceDTO> list(String search, int page, int size) {
        Pageable pageable = PageRequest.of(
            Math.max(page, 0),
            Math.max(1, Math.min(size, 100)),
            Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Device> p = (search == null || search.isBlank()) ?
            repo.findAll(pageable) :
            repo.findByNameContainingIgnoreCase(search.trim(), pageable);

        return p.map(d -> new DeviceDTO(d.getId(), d.getName(), d.getDescription(), d.getCreatedAt()));
    }

    public DeviceDTO create(CreateDeviceRequest req) {
        Device saved = repo.save(new Device(req.name(), req.description()));
        return new DeviceDTO(saved.getId(), saved.getName(), saved.getDescription(), saved.getCreatedAt());
    }
}
