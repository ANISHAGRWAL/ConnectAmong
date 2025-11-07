package com.connectamong.jobs;

import com.connectamong.common.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {
    
    private final JobService jobService;
    
    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @AuthenticationPrincipal CurrentUser currentUser,
            @Valid @RequestBody JobCreateRequest request) {
        JobResponse job = jobService.createJob(
                currentUser.userId(),
                currentUser.domain(),
                request);
        return ResponseEntity.ok(job);
    }
    
    @GetMapping
    public ResponseEntity<Page<JobResponse>> listJobs(
            @AuthenticationPrincipal CurrentUser currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<JobResponse> jobs = jobService.listJobs(
                currentUser.domain(),
                PageRequest.of(page, size));
        return ResponseEntity.ok(jobs);
    }
    
    @PostMapping("/{id}/apply")
    public ResponseEntity<Void> applyToJob(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationRequest request) {
        jobService.applyToJob(id, currentUser.userId(), currentUser.domain(), request);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/my-applications")
    public ResponseEntity<List<JobApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal CurrentUser currentUser) {
        List<JobApplicationResponse> applications = jobService.getMyApplications(currentUser.userId());
        return ResponseEntity.ok(applications);
    }
}
