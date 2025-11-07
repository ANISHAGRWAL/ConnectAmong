package com.connectamong.groups;

import com.connectamong.common.CurrentUser;
import com.connectamong.posts.PostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    
    private final GroupService groupService;
    
    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(
            @AuthenticationPrincipal CurrentUser currentUser,
            @Valid @RequestBody GroupCreateRequest request) {
        GroupResponse group = groupService.createGroup(
                currentUser.userId(),
                currentUser.domain(),
                request);
        return ResponseEntity.ok(group);
    }
    
    @GetMapping
    public ResponseEntity<Page<GroupResponse>> listGroups(
            @AuthenticationPrincipal CurrentUser currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<GroupResponse> groups = groupService.listGroups(
                currentUser.domain(),
                currentUser.userId(),
                PageRequest.of(page, size));
        return ResponseEntity.ok(groups);
    }
    
    @PostMapping("/{id}/join")
    public ResponseEntity<Void> joinGroup(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable Long id) {
        groupService.joinGroup(id, currentUser.userId(), currentUser.domain());
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}/leave")
    public ResponseEntity<Void> leaveGroup(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable Long id) {
        groupService.leaveGroup(id, currentUser.userId(), currentUser.domain());
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}/posts")
    public ResponseEntity<Page<PostResponse>> getGroupPosts(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PostResponse> posts = groupService.getGroupPosts(
                id,
                currentUser.domain(),
                PageRequest.of(page, size));
        return ResponseEntity.ok(posts);
    }
}
