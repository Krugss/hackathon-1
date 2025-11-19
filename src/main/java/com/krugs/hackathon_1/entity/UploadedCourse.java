package com.krugs.hackathon_1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "uploaded_courses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadedCourse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long runnerId;
    
    @Column(nullable = false)
    private String courseName;
    
    @Column
    private String theme;
    
    @Column(nullable = false)
    private Float distance;
    
    @Column(nullable = false)
    private String difficultyLevel;
    
    @Column(columnDefinition = "LONGTEXT")
    private String routePolyline;
    
    @Column
    private String gpxFileUrl;
    
    @Column
    private String description;
    
    // ✅ 경로 데이터 (JSON 형식으로 저장)
    @Column(columnDefinition = "LONGTEXT")
    private String coordinatesJson;
    
    // ✅ 고도 데이터 (JSON 형식으로 저장)
    @Column(columnDefinition = "LONGTEXT")
    private String elevationDataJson;
    
    @Column
    private Integer likeCount;
    
    @Column
    private Integer viewCount;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        likeCount = 0;
        viewCount = 0;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

