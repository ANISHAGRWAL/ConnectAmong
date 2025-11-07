package com.connectamong.jobs;

import com.connectamong.users.UserSummary;

import java.time.LocalDateTime;

public record JobResponse(
    Long id,
    String title,
    String company,
    String location,
    JobType type,
    String description,
    String applyUrl,
    UserSummary poster,
    LocalDateTime createdAt
) {
}
