package com.thinkquark.saa.util;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

public final class CursorCodec {
    private CursorCodec() {}

    public static String encode(Instant ts, Long id) {
        String payload = ts.toString() + "|" + id;
        return Base64.getUrlEncoder().withoutPadding()
            .encodeToString(payload.getBytes(StandardCharsets.UTF_8));
    }

    public static Decoded decode(String cursor) {
        if (cursor == null || cursor.isBlank()) {
            return null;
        }

        String raw = new String(Base64.getUrlDecoder().decode(cursor), StandardCharsets.UTF_8);
        String[] parts = raw.split("\\|", 2);

        return new Decoded(
            Instant.parse(parts[0]),
            Long.parseLong(parts[1])
        );
    }

    public record Decoded(Instant ts, Long id) { }
}
