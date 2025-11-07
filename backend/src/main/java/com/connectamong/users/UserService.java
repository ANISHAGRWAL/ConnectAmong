package com.connectamong.users;

import com.connectamong.common.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    @Transactional
    public UserProfile updateProfile(Long userId, UpdateProfileRequest request) {
        User user = findById(userId);
        user.setFullName(request.fullName());
        user.setSkills(request.skills());
        user.setBio(request.bio());
        user.setAvatarUrl(request.avatarUrl());
        user = userRepository.save(user);
        return UserProfile.from(user);
    }
    
    public Page<UserProfile> searchUsers(String domain, String query, Pageable pageable) {
        return userRepository.searchByDomain(domain, query, pageable)
                .map(UserProfile::from);
    }
}
