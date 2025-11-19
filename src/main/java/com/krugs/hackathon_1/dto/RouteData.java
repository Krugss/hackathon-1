package com.krugs.hackathon_1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteData {
    private String name;
    private String description;
    private Float distance;
    private Double startPointLat;  // ✅ 시작점 위도
    private Double startPointLng;  // ✅ 시작점 경도
    private List<List<Double>> coordinates;
    private List<String> landmarks;
    private Integer elevationGain;
    private Integer safetyScore;
    private String estimatedTime;
    private Integer rank;
}

