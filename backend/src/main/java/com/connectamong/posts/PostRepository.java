package com.connectamong.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    Page<Post> findByDomainOrderByCreatedAtDesc(String domain, Pageable pageable);
    
    Page<Post> findByDomainAndGroupIdOrderByCreatedAtDesc(String domain, Long groupId, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.domain = :domain AND (LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.body) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Post> searchByDomain(String domain, String query, Pageable pageable);
}
