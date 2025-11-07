package com.connectamong.jobs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    
    Optional<JobApplication> findByJobIdAndApplicantId(Long jobId, Long applicantId);
    
    List<JobApplication> findByApplicantIdOrderByCreatedAtDesc(Long applicantId);
}
