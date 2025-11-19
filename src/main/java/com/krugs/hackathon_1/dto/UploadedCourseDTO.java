package com.krugs.hackathon_1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadedCourseDTO {
    
    private Long id;
    private Long runnerId;
    private String courseName;
    private String theme;
    private Float distance;
    private String difficultyLevel;
    private String routePolyline;
    private String gpxFileUrl;
    private String description;
    private Integer likeCount;
    private Integer viewCount;
    
    // 경로 데이터 (좌표 배열)
    private List<Map<String, Double>> coordinates;
    
    // 고도 데이터 (배열)
    private List<Double> elevationData;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}

