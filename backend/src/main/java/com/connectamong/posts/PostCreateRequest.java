package com.connectamong.posts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostCreateRequest(
    @Size(max = 150, message = "Title must not exceed 150 characters")
    String title,
    
    @NotBlank(message = "Body is required")
    String body,
    
    @Size(max = 255, message = "Media URL must not exceed 255 characters")
    String mediaUrl,
    
    Long groupId
) {
}
