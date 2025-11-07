package com.connectamong.groups;

import java.time.LocalDateTime;

public record GroupResponse(
    Long id,
    String name,
    String description,
    GroupVisibility visibility,
    long memberCount,
    boolean isMember,
    LocalDateTime createdAt
) {
}
