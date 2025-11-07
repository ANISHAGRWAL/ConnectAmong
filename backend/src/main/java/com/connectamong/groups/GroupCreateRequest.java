package com.connectamong.groups;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GroupCreateRequest(
    @NotBlank(message = "Group name is required")
    @Size(max = 120, message = "Group name must not exceed 120 characters")
    String name,
    
    @Size(max = 300, message = "Description must not exceed 300 characters")
    String description
) {
}
