package com.krugs.hackathon_1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "marathon_events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarathonEvent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long organizerId;
    
    @Column(nullable = false)
    private String eventName;
    
    @Column
    private String theme;
    
    @Column
    private Float desiredDistance;
    
    @Column(nullable = false)
    private String startLocation;
    
    @Column
    private Double startLat;
    
    @Column
    private Double startLng;
    
    @Column(nullable = false)
    private String endLocation;
    
    @Column
    private Double endLat;
    
    @Column
    private Double endLng;
    
    @Column
    private String status;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = "pending";
        }
    }
}

