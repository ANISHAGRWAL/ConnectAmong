package com.connectamong.jobs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record JobCreateRequest(
    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must not exceed 150 characters")
    String title,
    
    @Size(max = 120, message = "Company must not exceed 120 characters")
    String company,
    
    @Size(max = 120, message = "Location must not exceed 120 characters")
    String location,
    
    JobType type,
    
    String description,
    
    @Size(max = 255, message = "Apply URL must not exceed 255 characters")
    String applyUrl
) {
}
