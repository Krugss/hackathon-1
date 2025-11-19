package com.krugs.hackathon_1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "running_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunningRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long runnerId;
    
    @Column
    private Long courseId;
    
    @Column(nullable = false)
    private Float distance;
    
    @Column(nullable = false)
    private Integer durationSeconds;
    
    @Column
    private Float avgPace;
    
    @Column
    private Integer caloriesBurned;
    
    @Column(columnDefinition = "LONGTEXT")
    private String routePolyline;
    
    @Column(columnDefinition = "LONGTEXT")
    private String gpsData;
    
    @Column
    private String notes;
    
    @Column(nullable = false)
    private LocalDateTime recordDate;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

