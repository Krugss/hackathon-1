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
public class RunningRecordDTO {
    
    private Long id;
    private Long runnerId;
    private Long courseId;
    private Float distance;
    private Integer durationSeconds;
    private Float avgPace;
    private Integer caloriesBurned;
    private String routePolyline;
    private List<Map<String, Object>> gpsData;
    private String notes;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime recordDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime createdAt;
    
    public String getFormattedDuration() {
        if (durationSeconds == null) return "0:00:00";
        long hours = durationSeconds / 3600;
        long minutes = (durationSeconds % 3600) / 60;
        long seconds = durationSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    
    public Integer getCaloriePerKm() {
        if (distance == null || distance == 0) return 0;
        return (int) (caloriesBurned / distance);
    }
}

