package com.connectamong.posts;

import com.connectamong.common.DomainAccessException;
import com.connectamong.common.ResourceNotFoundException;
import com.connectamong.users.User;
import com.connectamong.users.UserRepository;
import com.connectamong.users.UserSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;
    private final UserRepository userRepository;
    
    public Page<PostResponse> getFeed(String domain, Pageable pageable) {
        return postRepository.findByDomainOrderByCreatedAtDesc(domain, pageable)
                .map(this::toPostResponse);
    }
    
    @Transactional
    public PostResponse createPost(Long userId, String userDomain, PostCreateRequest request) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // If groupId is provided, we should validate it belongs to same domain
        // For now, we'll skip group validation and just set the domain
        
        Post post = Post.builder()
                .author(author)
                .domain(userDomain)
                .groupId(request.groupId())
                .title(request.title())
                .body(request.body())
                .mediaUrl(request.mediaUrl())
                .build();
        
        post = postRepository.save(post);
        return toPostResponse(post);
    }
    
    public PostResponse getPost(Long postId, String userDomain) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        if (!post.getDomain().equals(userDomain)) {
            throw new DomainAccessException("Cannot access post from different domain");
        }
        
        return toPostResponse(post);
    }
    
    @Transactional
    public CommentResponse addComment(Long postId, Long userId, String userDomain, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        if (!post.getDomain().equals(userDomain)) {
            throw new DomainAccessException("Cannot comment on post from different domain");
        }
        
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Comment comment = Comment.builder()
                .post(post)
                .author(author)
                .body(request.body())
                .build();
        
        comment = commentRepository.save(comment);
        return toCommentResponse(comment);
    }
    
    @Transactional
    public void addReaction(Long postId, Long userId, String userDomain, ReactionType type) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        if (!post.getDomain().equals(userDomain)) {
            throw new DomainAccessException("Cannot react to post from different domain");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Check if reaction already exists
        if (reactionRepository.findByUserIdAndPostIdAndType(userId, postId, type).isPresent()) {
            return; // Already reacted
        }
        
        Reaction reaction = Reaction.builder()
                .user(user)
                .post(post)
                .type(type)
                .build();
        
        reactionRepository.save(reaction);
    }
    
    @Transactional
    public void removeReaction(Long postId, Long userId, String userDomain, ReactionType type) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        if (!post.getDomain().equals(userDomain)) {
            throw new DomainAccessException("Cannot unreact to post from different domain");
        }
        
        reactionRepository.deleteByUserIdAndPostIdAndType(userId, postId, type);
    }
    
    public Page<PostResponse> searchPosts(String domain, String query, Pageable pageable) {
        return postRepository.searchByDomain(domain, query, pageable)
                .map(this::toPostResponse);
    }
    
    private PostResponse toPostResponse(Post post) {
        long likes = reactionRepository.countByPostIdAndType(post.getId(), ReactionType.LIKE);
        long comments = commentRepository.countByPostId(post.getId());
        
        return new PostResponse(
                post.getId(),
                UserSummary.from(post.getAuthor()),
                post.getGroupId(),
                post.getTitle(),
                post.getBody(),
                post.getMediaUrl(),
                new PostCounts(likes, comments),
                post.getCreatedAt()
        );
    }
    
    private CommentResponse toCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                UserSummary.from(comment.getAuthor()),
                comment.getBody(),
                comment.getCreatedAt()
        );
    }
}
