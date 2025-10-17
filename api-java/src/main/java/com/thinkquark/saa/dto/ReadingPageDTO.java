package com.thinkquark.saa.dto;

import java.util.List;

public record ReadingPageDTO(
    List<ReadingDTO> items,
    String nextCursor
) { }
