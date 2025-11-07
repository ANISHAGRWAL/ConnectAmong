package com.connectamong.jobs;

import jakarta.validation.constraints.Size;

public record JobApplicationRequest(
    @Size(max = 600, message = "Cover note must not exceed 600 characters")
    String coverNote
) {
}
