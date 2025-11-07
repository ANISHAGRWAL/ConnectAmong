package com.connectamong.groups;

import com.connectamong.users.User;
import com.connectamong.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
    
    @Mock
    private GroupRepository groupRepository;
    
    @Mock
    private GroupMemberRepository groupMemberRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private com.connectamong.posts.PostRepository postRepository;
    
    @InjectMocks
    private GroupService groupService;
    
    @Test
    void testJoinGroup() {
        // Arrange
        Long groupId = 1L;
        Long userId = 2L;
        String userDomain = "DEVELOPER";
        
        User user = User.builder()
                .id(userId)
                .domain(userDomain)
                .build();
        
        Group group = Group.builder()
                .id(groupId)
                .domain(userDomain)
                .build();
        
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(groupMemberRepository.existsByGroupIdAndUserId(groupId, userId)).thenReturn(false);
        when(groupMemberRepository.save(any(GroupMember.class))).thenAnswer(i -> i.getArgument(0));
        
        // Act
        groupService.joinGroup(groupId, userId, userDomain);
        
        // Assert
        verify(groupMemberRepository).save(any(GroupMember.class));
    }
    
    @Test
    void testLeaveGroup() {
        // Arrange
        Long groupId = 1L;
        Long userId = 2L;
        String userDomain = "DEVELOPER";
        
        Group group = Group.builder()
                .id(groupId)
                .domain(userDomain)
                .build();
        
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        
        // Act
        groupService.leaveGroup(groupId, userId, userDomain);
        
        // Assert
        verify(groupMemberRepository).deleteByGroupIdAndUserId(groupId, userId);
    }
}
