package com.connectamong.users;

import com.connectamong.common.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<UserProfile> getCurrentUser(
            @AuthenticationPrincipal CurrentUser currentUser) {
        User user = userService.findById(currentUser.userId());
        return ResponseEntity.ok(UserProfile.from(user));
    }
    
    @PutMapping
    public ResponseEntity<UserProfile> updateProfile(
            @AuthenticationPrincipal CurrentUser currentUser,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserProfile updated = userService.updateProfile(currentUser.userId(), request);
        return ResponseEntity.ok(updated);
    }
}
