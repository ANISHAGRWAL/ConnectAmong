package com.connectamong.jobs;

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
public class JobService {
    
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public JobResponse createJob(Long userId, String userDomain, JobCreateRequest request) {
        User poster = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Job job = Job.builder()
                .domain(userDomain)
                .poster(poster)
                .title(request.title())
                .company(request.company())
                .location(request.location())
                .type(request.type() != null ? request.type() : JobType.FULLTIME)
                .description(request.description())
                .applyUrl(request.applyUrl())
                .build();
        
        job = jobRepository.save(job);
        return toJobResponse(job);
    }
    
    public Page<JobResponse> listJobs(String domain, Pageable pageable) {
        return jobRepository.findByDomainOrderByCreatedAtDesc(domain, pageable)
                .map(this::toJobResponse);
    }
    
    @Transactional
    public void applyToJob(Long jobId, Long userId, String userDomain, JobApplicationRequest request) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        
        if (!job.getDomain().equals(userDomain)) {
            throw new DomainAccessException("Cannot apply to job from different domain");
        }
        
        // Check if already applied
        if (jobApplicationRepository.findByJobIdAndApplicantId(jobId, userId).isPresent()) {
            throw new IllegalStateException("Already applied to this job");
        }
        
        User applicant = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        JobApplication application = JobApplication.builder()
                .job(job)
                .applicant(applicant)
                .coverNote(request.coverNote())
                .build();
        
        jobApplicationRepository.save(application);
    }
    
    public List<JobApplicationResponse> getMyApplications(Long userId) {
        return jobApplicationRepository.findByApplicantIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toJobApplicationResponse)
                .toList();
    }
    
    private JobResponse toJobResponse(Job job) {
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                job.getLocation(),
                job.getType(),
                job.getDescription(),
                job.getApplyUrl(),
                UserSummary.from(job.getPoster()),
                job.getCreatedAt()
        );
    }
    
    private JobApplicationResponse toJobApplicationResponse(JobApplication application) {
        return new JobApplicationResponse(
                application.getId(),
                toJobResponse(application.getJob()),
                application.getCoverNote(),
                application.getCreatedAt()
        );
    }
}
