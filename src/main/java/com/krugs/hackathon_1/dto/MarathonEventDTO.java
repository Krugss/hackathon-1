package com.krugs.hackathon_1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarathonEventDTO {
    
    private Long id;
    private Long organizerId;
    private String eventName;
    private String theme;
    private Float desiredDistance;
    private String startLocation;
    private String endLocation;
    private Double startLat;
    private Double startLng;
    private Double endLat;
    private Double endLng;
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime createdAt;
}

