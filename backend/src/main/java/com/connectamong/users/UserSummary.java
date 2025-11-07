package com.connectamong.users;

public record UserSummary(
    Long id,
    String fullName,
    String avatarUrl
) {
    public static UserSummary from(User user) {
        return new UserSummary(user.getId(), user.getFullName(), user.getAvatarUrl());
    }
}
