package com.thinkquark.saa.repos;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thinkquark.saa.entities.Reading;

public interface ReadingRepo extends JpaRepository<Reading, Long> {
    @Query("""
        SELECT r FROM Reading r
        WHERE r.device.id = :deviceId
          AND r.ts >= COALESCE(:fromTs, r.ts)
          AND r.ts <= COALESCE(:toTs, r.ts)
          AND r.severity >= COALESCE(:minSeverity, r.severity)
        ORDER BY r.ts DESC, r.id DESC
    """)
    List<Reading> firstPage(
        UUID deviceId,
        Instant fromTs,
        Instant toTs,
        Integer minSeverity,
        Pageable pageable
    );

    @Query("""
        SELECT r FROM Reading r
        WHERE r.device.id = :deviceId
          AND r.ts >= COALESCE(:fromTs, r.ts)
          AND r.ts <= COALESCE(:toTs, r.ts)
          AND r.severity >= COALESCE(:minSeverity, r.severity)
          AND ( (r.ts < :cursorTs) OR (r.ts = :cursorTs AND r.id < :cursorId) )
        ORDER BY r.ts DESC, r.id DESC
    """)
    List<Reading> nextPage(
        UUID deviceId,
        Instant fromTs,
        Instant toTs,
        Integer minSeverity,
        Instant cursorTs,
        Long cursorId,
        Pageable pageable
    );
}
