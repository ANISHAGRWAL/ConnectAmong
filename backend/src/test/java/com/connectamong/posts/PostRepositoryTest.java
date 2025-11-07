package com.connectamong.posts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PostRepositoryTest {
    
    @Autowired
    private PostRepository postRepository;
    
    @Test
    void testFindByDomain() {
        // This test will work when database is available
        // For now, it's a placeholder to demonstrate domain filtering capability
        Page<Post> posts = postRepository.findByDomainOrderByCreatedAtDesc(
                "DEVELOPER", 
                PageRequest.of(0, 10));
        
        assertNotNull(posts);
    }
}
