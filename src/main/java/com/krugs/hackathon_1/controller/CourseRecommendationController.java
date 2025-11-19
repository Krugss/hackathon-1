package com.krugs.hackathon_1.controller;

import com.krugs.hackathon_1.dto.RouteData;
import com.krugs.hackathon_1.service.CourseRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseRecommendationController {

    private final CourseRecommendationService courseRecommendationService;

    /**
     * Gemini API를 통해 추천 코스 생성
     * POST /api/courses/recommend
     */
    @PostMapping("/recommend")
    public ResponseEntity<?> recommendCourses(
            @RequestParam String theme,
            @RequestParam Float distance,
            @RequestParam String startLocation,
            @RequestParam String endLocation,
            @RequestParam(defaultValue = "intermediate") String difficulty) {

        try {
            List<RouteData> routes = courseRecommendationService.generateOptimalRoutes(
                    theme, distance, startLocation, endLocation, difficulty);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", routes);
            response.put("count", routes.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * GET 방식으로도 추천 받기 (파라미터 기본값 적용)
     * GET /api/courses/recommend?theme=공원&distance=10&startLocation=대전&difficulty=beginner
     */
    @GetMapping("/recommend")
    public ResponseEntity<?> recommendCoursesGet(
            @RequestParam(required = false, defaultValue = "공원") String theme,
            @RequestParam(required = false, defaultValue = "10") String distanceStr,
            @RequestParam(required = false, defaultValue = "대전") String startLocation,
            @RequestParam(required = false, defaultValue = "") String endLocation,
            @RequestParam(required = false, defaultValue = "intermediate") String difficulty) {

        try {
            // ✅ 1️⃣ 파라미터 정제 (trim)
            theme = (theme != null) ? theme.trim() : "공원";
            startLocation = (startLocation != null) ? startLocation.trim() : "대전";
            distanceStr = (distanceStr != null) ? distanceStr.trim() : "10";
            endLocation = (endLocation != null) ? endLocation.trim() : "";

            // ✅ 2️⃣ 필수 파라미터 기본값 적용
            if (theme.isEmpty()) {
                theme = "공원";
            }
            if (startLocation.isEmpty()) {
                startLocation = "대전";
            }

            // ✅ 3️⃣ String → Float 변환 (오류 처리 + 기본값)
            Float distance = 10.0f;  // 기본값: 10km
            try {
                distance = Float.parseFloat(distanceStr);
                if (distance <= 0) {
                    System.out.println("⚠️ 거리가 0 이하입니다: " + distance + " → 기본값(10km) 사용");
                    distance = 10.0f;
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ 거리 변환 실패: " + distanceStr + " → 기본값(10km) 사용");
                distance = 10.0f;
            }

            // ✅ 4️⃣ endLocation이 없으면 startLocation 사용
            String actualEndLocation = (endLocation == null || endLocation.isEmpty()) ? startLocation : endLocation;
            
            // ✅ 5️⃣ 난이도 유효성 검사 및 기본값
            if (!difficulty.matches("beginner|intermediate|advanced")) {
                System.out.println("⚠️ 유효하지 않은 난이도: " + difficulty + " → 기본값(intermediate) 사용");
                difficulty = "intermediate";
            }
            
            System.out.println("📡 API 요청 수신 (기본값 적용됨):");
            System.out.println("   테마: " + theme);
            System.out.println("   거리: " + distance + "km");
            System.out.println("   시작점: " + startLocation);
            System.out.println("   종료점: " + actualEndLocation);
            System.out.println("   난이도: " + difficulty);
            
            List<RouteData> routes = courseRecommendationService.generateOptimalRoutes(
                    theme, distance, startLocation, actualEndLocation, difficulty);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", routes);
            response.put("count", routes.size());
            response.put("message", routes.size() + "개의 추천 코스가 생성되었습니다");

            System.out.println("✅ 응답 생성 완료: " + routes.size() + "개 코스");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ 서버 오류: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "코스 생성 실패: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}

