package com.connectamong.groups;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    
    boolean existsByGroupIdAndUserId(Long groupId, Long userId);
    
    void deleteByGroupIdAndUserId(Long groupId, Long userId);
    
    long countByGroupId(Long groupId);
}
