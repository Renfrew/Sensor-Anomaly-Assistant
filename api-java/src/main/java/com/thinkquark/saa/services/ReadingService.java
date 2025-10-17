package com.thinkquark.saa.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkquark.saa.dto.ReadingDTO;
import com.thinkquark.saa.dto.ReadingIngestDTO;
import com.thinkquark.saa.dto.ReadingPageDTO;
import com.thinkquark.saa.entities.Device;
import com.thinkquark.saa.entities.Reading;
import com.thinkquark.saa.repos.DeviceRepo;
import com.thinkquark.saa.repos.ReadingRepo;
import com.thinkquark.saa.util.CursorCodec;

@Service
public class ReadingService {
    private final ReadingRepo readings;
    private final DeviceRepo devices;

    public ReadingService(ReadingRepo readings, DeviceRepo devices) {
        this.readings = readings;
        this.devices = devices;
    }

    @Transactional(readOnly = true)
    public ReadingPageDTO list(
            UUID deviceId,
            Instant fromTs,
            Instant toTs,
            Integer minSeverity,
            String cursor,
            int limit) {

        int safeLimit = Math.max(1, Math.min(limit, 200));
        Pageable pageable = PageRequest.of(
                0,
                safeLimit,
                Sort.by(Sort.Direction.DESC, "ts").and(Sort.by(Sort.Direction.DESC, "id")));

        List<Reading> rows;
        CursorCodec.Decoded decoded = CursorCodec.decode(cursor);

        if (decoded == null) {
            rows = readings.firstPage(
                    deviceId,
                    fromTs,
                    toTs,
                    minSeverity,
                    pageable);
        } else {
            rows = readings.nextPage(
                    deviceId,
                    fromTs,
                    toTs,
                    minSeverity,
                    decoded.ts(),
                    decoded.id(),
                    pageable);
        }

        String next = null;
        if (rows.size() == safeLimit) {
            Reading last = rows.get(rows.size() - 1);
            next = CursorCodec.encode(last.getTs(), last.getId());
        }

        List<ReadingDTO> items = rows.stream()
                .map(r -> new ReadingDTO(
                        r.getId(),
                        r.getDevice().getId(),
                        r.getTs(),
                        r.getValue(),
                        r.getSeverity(),
                        r.getReason()))
                .toList();

        return new ReadingPageDTO(items, next);
    }

    @Transactional
    public void ingest(List<ReadingIngestDTO> payload) {
        for (ReadingIngestDTO dto : payload) {
            Device device = devices.findById(dto.deviceId())
                    .orElseThrow(() -> new IllegalArgumentException("Unknown device ID: " + dto.deviceId()));

            int sev = dto.severity() != null ? dto.severity() : 0;

            Reading reading = new Reading(
                    device,
                    dto.ts(),
                    dto.value(),
                    sev,
                    dto.reason());

            readings.save(reading);
        }
    }
}
