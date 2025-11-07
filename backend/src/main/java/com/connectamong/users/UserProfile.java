package com.connectamong.users;

import java.time.LocalDateTime;

public record UserProfile(
    Long id,
    String email,
    String fullName,
    String domain,
    String skills,
    String bio,
    String avatarUrl,
    LocalDateTime createdAt
) {
    public static UserProfile from(User user) {
        return new UserProfile(
            user.getId(),
            user.getEmail(),
            user.getFullName(),
            user.getDomain(),
            user.getSkills(),
            user.getBio(),
            user.getAvatarUrl(),
            user.getCreatedAt()
        );
    }
}
