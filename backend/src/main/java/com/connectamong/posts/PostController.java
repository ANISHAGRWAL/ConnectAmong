package com.connectamong.posts;

import com.connectamong.common.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;
    
    @GetMapping("/feed")
    public ResponseEntity<Page<PostResponse>> getFeed(
            @AuthenticationPrincipal CurrentUser currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PostResponse> feed = postService.getFeed(
                currentUser.domain(),
                PageRequest.of(page, size));
        return ResponseEntity.ok(feed);
    }
    
    @PostMapping("/posts")
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal CurrentUser currentUser,
            @Valid @RequestBody PostCreateRequest request) {
        PostResponse post = postService.createPost(
                currentUser.userId(),
                currentUser.domain(),
                request);
        return ResponseEntity.ok(post);
    }
    
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponse> getPost(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable Long id) {
        PostResponse post = postService.getPost(id, currentUser.domain());
        return ResponseEntity.ok(post);
    }
    
    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request) {
        CommentResponse comment = postService.addComment(
                id,
                currentUser.userId(),
                currentUser.domain(),
                request);
        return ResponseEntity.ok(comment);
    }
    
    @PostMapping("/posts/{id}/react")
    public ResponseEntity<Void> addReaction(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable Long id,
            @RequestParam ReactionType type) {
        postService.addReaction(id, currentUser.userId(), currentUser.domain(), type);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/posts/{id}/react")
    public ResponseEntity<Void> removeReaction(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable Long id,
            @RequestParam ReactionType type) {
        postService.removeReaction(id, currentUser.userId(), currentUser.domain(), type);
        return ResponseEntity.ok().build();
    }
}
