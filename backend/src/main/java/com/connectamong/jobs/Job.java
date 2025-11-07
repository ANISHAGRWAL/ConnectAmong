package com.connectamong.jobs;

import com.connectamong.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String domain;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id", nullable = false)
    private User poster;
    
    @Column(nullable = false, length = 150)
    private String title;
    
    @Column(length = 120)
    private String company;
    
    @Column(length = 120)
    private String location;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType type;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "apply_url", length = 255)
    private String applyUrl;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (type == null) {
            type = JobType.FULLTIME;
        }
    }
}
