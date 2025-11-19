package com.krugs.hackathon_1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 지역별 관광 정보 및 특산물 데이터
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionInfo {
    private String regionName;          // 지역명 (대전, 아산, 공주, 부여, 태안 등)
    private double centerLat;           // 지역 중심 위도
    private double centerLng;           // 지역 중심 경도
    private List<Attraction> attractions;  // 주요 관광지 목록
    private List<String> specialties;   // 특산물 목록
    private List<String> themes;        // 지역의 추천 테마
    private String description;         // 지역 설명
}

