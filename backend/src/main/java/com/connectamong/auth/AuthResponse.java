package com.connectamong.auth;

public record AuthResponse(
    String token,
    String message
) {
}
