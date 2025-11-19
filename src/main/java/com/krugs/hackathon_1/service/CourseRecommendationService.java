package com.krugs.hackathon_1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krugs.hackathon_1.dto.RouteData;
import com.krugs.hackathon_1.dto.RegionInfo;
import com.krugs.hackathon_1.dto.Attraction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseRecommendationService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final RegionDataService regionDataService;

    /**
     * Gemini API를 통해 최적 마라톤 경로 생성
     */
    public List<RouteData> generateOptimalRoutes(
            String theme,
            Float distance,
            String startLocation,
            String endLocation,
            String difficulty) {

        try {
            String prompt = buildPrompt(theme, distance, startLocation, endLocation, difficulty);
            log.info("Generated prompt for theme: {}, distance: {}km", theme, distance);

            String geminiResponse = callGeminiAPI(prompt);
            log.info("Gemini API response received");

            List<RouteData> routes = parseGeminiResponse(geminiResponse);
            log.info("Parsed {} routes from Gemini response", routes.size());

            return routes;

        } catch (Exception e) {
            log.error("Error generating optimal routes", e);
            throw new RuntimeException("경로 생성 실패: " + e.getMessage());
        }
    }

    /**
     * 프롬프트 생성 - 동적 템플릿 기반
     */
    private String buildPrompt(String theme, Float distance, String startLocation,
                              String endLocation, String difficulty) {
        // 시작 위치 기반으로 지역 정보 조회
        RegionInfo region = regionDataService.getRegionByStartLocation(startLocation);
        
        // 관광지 정보 생성
        StringBuilder attractionsInfo = new StringBuilder();
        for (Attraction attraction : region.getAttractions()) {
            attractionsInfo.append(String.format("- %s: %.4f, %.4f (%s)\n", 
                    attraction.getName(), 
                    attraction.getLatitude(), 
                    attraction.getLongitude(),
                    attraction.getCategory()));
        }
        
        // 특산물 정보 생성
        String specialtiesInfo = String.join(", ", region.getSpecialties());
        
        return String.format("""
            당신은 대전·충청남도 지역의 최적 관광 마라톤 코스 설계 전문가입니다.
            
            다음 조건에 맞는 최적의 관광 마라톤 코스를 3가지 설계해주세요:
            
            **필수 조건:**
            - 테마: %s
            - 거리: %.2f km
            - 출발지: %s (좌표: %.4f, %.4f)
            - 목적지: %s
            - 난이도: %s
            - 지역: %s
            
            **지역 정보:**
            지역명: %s
            지역 중심: %.4f, %.4f
            지역 설명: %s
            
            **주요 관광지:**
            %s
            **지역 특산물:**
            %s
            
            **설계 기준:**
            1. 경로 설계:
               - 거리에 맞춰 %d~%d개 포인트 생성
               - 시작점 좌표 (%.4f, %.4f)에서 시작하여 관광지들을 거쳐 순환 또는 선형 코스
               - 실제 도로망을 고려한 자연스러운 경로
               - ✅ 반드시 startPointLat, startPointLng 필드를 추가하여 시작점 좌표 포함
            
            2. 테마별 특화:
               - 주요 관광지 통과 또는 근접 (반드시 포함)
               - 지역 특산물과 관련 체험지 연결
               - 사진 명소와 쉼터 포함
            
            3. 난이도별 고도 변화:
               - 초급: 고도 변화 80m 미만 (평탄한 해변/온천 코스)
               - 중급: 고도 변화 150~250m (도시+산악 혼합)
               - 고급: 고도 변화 300m 이상 (산악 위주)
            
            4. 코스 제약:
               - 5~7km마다 휴식/편의점 위치 배치
               - 조명 좋은 도시/국도 우선
               - 안전한 보행로 고려
            
            **응답 형식 (JSON만 응답):**
            {
              "routes": [
                {
                  "name": "코스 이름",
                  "description": "코스 설명 (테마와 특산물 포함)",
                  "distance": 10.5,
                  "startPointLat": 36.3742,
                  "startPointLng": 127.3515,
                  "coordinates": [
                    [위도, 경도],
                    [위도, 경도],
                    ...
                  ],
                  "landmarks": ["관광지1", "관광지2", "특산물"],
                  "elevationGain": 65,
                  "safetyScore": 8,
                  "estimatedTime": "1:40"
                },
                {
                  "name": "코스 이름",
                  "description": "코스 설명",
                  "distance": 12.0,
                  "startPointLat": 36.3742,
                  "startPointLng": 127.3515,
                  "coordinates": [...],
                  "landmarks": [...],
                  "elevationGain": 145,
                  "safetyScore": 8,
                  "estimatedTime": "1:55"
                },
                {
                  "name": "코스 이름",
                  "description": "코스 설명",
                  "distance": 15.0,
                  "startPointLat": 36.3742,
                  "startPointLng": 127.3515,
                  "coordinates": [...],
                  "landmarks": [...],
                  "elevationGain": 240,
                  "safetyScore": 8,
                  "estimatedTime": "2:20"
                }
              ]
            }
            
            **중요 지시사항:**
            - 오직 JSON 형식만 응답하세요
            - 마크다운 코드 블록(```) 사용 금지
            - 설명이나 인사말 절대 금지
            - 각 coordinates 배열은 [위도, 경도] 형태
            - 거리와 고도는 실제 지형에 맞게 제시하세요
            - 반드시 3개의 서로 다른 코스를 제시하세요
            - ✅ 각 코스에 startPointLat, startPointLng 필드를 포함하세요 (시작점 좌표)
            """,
            theme, distance, startLocation, region.getCenterLat(), region.getCenterLng(), 
            endLocation, difficulty,
            region.getRegionName(),
            region.getRegionName(), 
            region.getCenterLat(), 
            region.getCenterLng(),
            region.getDescription(),
            attractionsInfo.toString(),
            specialtiesInfo,
            (int)(distance * 1.5), (int)(distance * 2.5),
            region.getCenterLat(), region.getCenterLng());
    }

    /**
     * Gemini API 호출
     */
    private String callGeminiAPI(String prompt) {
        try {
            System.out.println("🤖 Gemini API 호출 중...");
            System.out.println("프롬프트: " + prompt.substring(0, Math.min(100, prompt.length())) + "...");
            
            Content content = Content.fromParts(
                    Part.fromText(prompt)
            );
            
            // ✅ API 키를 포함하여 Client 초기화
            Client client = new Client.Builder()
                    .apiKey(geminiApiKey)
                    .build();
            
            // API 호출
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.0-flash",
                    content,
                    null
            );

            String result = response.text();
            System.out.println("✅ Gemini API 응답 받음");
            System.out.println("응답 길이: " + result.length() + "자");
            
            return result;

        } catch (Exception e) {
            System.out.println("❌ Gemini API 호출 실패: " + e.getMessage());
            e.printStackTrace();
            log.error("Gemini API call failed", e);
            throw new RuntimeException("Gemini API 호출 실패: " + e.getMessage());
        }
    }

    /**
     * Gemini 응답 파싱
     */
    private List<RouteData> parseGeminiResponse(String response) {
        try {
            String cleanedResponse = response.trim();
            
            // 마크다운 코드 블록 제거
            if (cleanedResponse.startsWith("```json")) {
                cleanedResponse = cleanedResponse.replace("```json", "").replace("```", "").trim();
            } else if (cleanedResponse.startsWith("```")) {
                cleanedResponse = cleanedResponse.replace("```", "").trim();
            }

            Map<String, Object> jsonResponse = objectMapper.readValue(cleanedResponse, Map.class);
            List<Map<String, Object>> routesJson = (List<Map<String, Object>>) jsonResponse.get("routes");

            List<RouteData> routes = new ArrayList<>();
            if (routesJson != null) {
                for (int i = 0; i < Math.min(routesJson.size(), 3); i++) {
                    Map<String, Object> routeJson = routesJson.get(i);
                    
                    RouteData route = RouteData.builder()
                        .name((String) routeJson.get("name"))
                        .description((String) routeJson.get("description"))
                        .distance(((Number) routeJson.get("distance")).floatValue())
                        .coordinates((List<List<Double>>) routeJson.get("coordinates"))
                        .landmarks((List<String>) routeJson.get("landmarks"))
                        .elevationGain(((Number) routeJson.getOrDefault("elevationGain", 0)).intValue())
                        .safetyScore(((Number) routeJson.getOrDefault("safetyScore", 7)).intValue())
                        .estimatedTime((String) routeJson.get("estimatedTime"))
                        .rank(i + 1)
                        .build();
                    
                    routes.add(route);
                }
            }

            return routes;

        } catch (Exception e) {
            log.error("Failed to parse Gemini response", e);
            throw new RuntimeException("응답 파싱 실패: " + e.getMessage());
        }
    }
}
