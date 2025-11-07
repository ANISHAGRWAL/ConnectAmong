package com.connectamong.posts;

import com.connectamong.users.UserSummary;

import java.time.LocalDateTime;

public record CommentResponse(
    Long id,
    UserSummary author,
    String body,
    LocalDateTime createdAt
) {
}
