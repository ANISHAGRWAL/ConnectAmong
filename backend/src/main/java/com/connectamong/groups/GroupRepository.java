package com.connectamong.groups;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    
    Page<Group> findByDomainOrderByCreatedAtDesc(String domain, Pageable pageable);
}
