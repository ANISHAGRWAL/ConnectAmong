package com.connectamong.groups;

import com.connectamong.common.DomainAccessException;
import com.connectamong.common.ResourceNotFoundException;
import com.connectamong.posts.PostResponse;
import com.connectamong.posts.PostRepository;
import com.connectamong.users.User;
import com.connectamong.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {
    
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    
    @Transactional
    public GroupResponse createGroup(Long userId, String userDomain, GroupCreateRequest request) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Group group = Group.builder()
                .name(request.name())
                .domain(userDomain)
                .description(request.description())
                .visibility(GroupVisibility.PUBLIC)
                .creator(creator)
                .build();
        
        group = groupRepository.save(group);
        
        // Auto-join creator
        GroupMember member = GroupMember.builder()
                .group(group)
                .user(creator)
                .role(MemberRole.MEMBER)
                .build();
        groupMemberRepository.save(member);
        
        return toGroupResponse(group, userId);
    }
    
    public Page<GroupResponse> listGroups(String domain, Long userId, Pageable pageable) {
        return groupRepository.findByDomainOrderByCreatedAtDesc(domain, pageable)
                .map(group -> toGroupResponse(group, userId));
    }
    
    @Transactional
    public void joinGroup(Long groupId, Long userId, String userDomain) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        
        if (!group.getDomain().equals(userDomain)) {
            throw new DomainAccessException("Cannot join group from different domain");
        }
        
        if (groupMemberRepository.existsByGroupIdAndUserId(groupId, userId)) {
            return; // Already a member
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        GroupMember member = GroupMember.builder()
                .group(group)
                .user(user)
                .role(MemberRole.MEMBER)
                .build();
        
        groupMemberRepository.save(member);
    }
    
    @Transactional
    public void leaveGroup(Long groupId, Long userId, String userDomain) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        
        if (!group.getDomain().equals(userDomain)) {
            throw new DomainAccessException("Cannot leave group from different domain");
        }
        
        groupMemberRepository.deleteByGroupIdAndUserId(groupId, userId);
    }
    
    public Page<PostResponse> getGroupPosts(Long groupId, String userDomain, Pageable pageable) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        
        if (!group.getDomain().equals(userDomain)) {
            throw new DomainAccessException("Cannot view posts from group in different domain");
        }
        
        return postRepository.findByDomainAndGroupIdOrderByCreatedAtDesc(userDomain, groupId, pageable)
                .map(post -> {
                    long likes = 0; // simplified for now
                    long comments = 0;
                    return new PostResponse(
                            post.getId(),
                            new com.connectamong.users.UserSummary(
                                    post.getAuthor().getId(),
                                    post.getAuthor().getFullName(),
                                    post.getAuthor().getAvatarUrl()
                            ),
                            post.getGroupId(),
                            post.getTitle(),
                            post.getBody(),
                            post.getMediaUrl(),
                            new com.connectamong.posts.PostCounts(likes, comments),
                            post.getCreatedAt()
                    );
                });
    }
    
    private GroupResponse toGroupResponse(Group group, Long userId) {
        long memberCount = groupMemberRepository.countByGroupId(group.getId());
        boolean isMember = groupMemberRepository.existsByGroupIdAndUserId(group.getId(), userId);
        
        return new GroupResponse(
                group.getId(),
                group.getName(),
                group.getDescription(),
                group.getVisibility(),
                memberCount,
                isMember,
                group.getCreatedAt()
        );
    }
}
