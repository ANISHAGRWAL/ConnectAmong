package com.connectamong.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    String fullName,
    
    @Size(max = 255, message = "Skills must not exceed 255 characters")
    String skills,
    
    @Size(max = 500, message = "Bio must not exceed 500 characters")
    String bio,
    
    @Size(max = 255, message = "Avatar URL must not exceed 255 characters")
    String avatarUrl
) {
}
