package com.connectamong.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    
    Optional<Reaction> findByUserIdAndPostIdAndType(Long userId, Long postId, ReactionType type);
    
    long countByPostIdAndType(Long postId, ReactionType type);
    
    void deleteByUserIdAndPostIdAndType(Long userId, Long postId, ReactionType type);
}
