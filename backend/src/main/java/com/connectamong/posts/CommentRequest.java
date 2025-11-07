package com.connectamong.posts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
    @NotBlank(message = "Comment body is required")
    @Size(max = 800, message = "Comment must not exceed 800 characters")
    String body
) {
}
