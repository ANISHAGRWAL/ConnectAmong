package com.connectamong.search;

import com.connectamong.common.CurrentUser;
import com.connectamong.posts.PostResponse;
import com.connectamong.posts.PostService;
import com.connectamong.users.UserProfile;
import com.connectamong.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    
    private final PostService postService;
    private final UserService userService;
    
    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponse>> searchPosts(
            @AuthenticationPrincipal CurrentUser currentUser,
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PostResponse> posts = postService.searchPosts(
                currentUser.domain(),
                q,
                PageRequest.of(page, size));
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/users")
    public ResponseEntity<Page<UserProfile>> searchUsers(
            @AuthenticationPrincipal CurrentUser currentUser,
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<UserProfile> users = userService.searchUsers(
                currentUser.domain(),
                q,
                PageRequest.of(page, size));
        return ResponseEntity.ok(users);
    }
}
