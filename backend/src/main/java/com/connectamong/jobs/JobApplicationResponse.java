package com.connectamong.jobs;

import java.time.LocalDateTime;

public record JobApplicationResponse(
    Long id,
    JobResponse job,
    String coverNote,
    LocalDateTime createdAt
) {
}
