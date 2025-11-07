package com.connectamong.posts;

import com.connectamong.users.UserSummary;

import java.time.LocalDateTime;

public record PostResponse(
    Long id,
    UserSummary author,
    Long groupId,
    String title,
    String body,
    String mediaUrl,
    PostCounts counts,
    LocalDateTime createdAt
) {
}
