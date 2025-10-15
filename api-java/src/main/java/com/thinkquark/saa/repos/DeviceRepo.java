package com.thinkquark.saa.repos;

import com.thinkquark.saa.entities.Device;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeviceRepo extends JpaRepository<Device, UUID> {
	Page<Device> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
