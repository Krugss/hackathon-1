package com.krugs.hackathon_1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 관광지 정보
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attraction {
    private String name;                // 관광지명
    private double latitude;            // 위도
    private double longitude;           // 경도
    private String category;            // 카테고리 (문화유산, 자연경관, 온천, 해변 등)
    private String description;         // 설명
}

