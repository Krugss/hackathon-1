package com.krugs.hackathon_1.controller;

import com.krugs.hackathon_1.dto.MarathonEventDTO;
import com.krugs.hackathon_1.dto.RouteData;
import com.krugs.hackathon_1.service.MarathonEventService;
import com.krugs.hackathon_1.service.CourseRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/organizer/marathons")
@RequiredArgsConstructor
public class MarathonEventController {
    
    private final MarathonEventService marathonService;
    private final CourseRecommendationService courseRecommendationService;
    
    /**
     * 마라톤 코스 추천 (Gemini API)
     * GET /api/organizer/marathons/recommend
     */
    @GetMapping("/recommend")
    public ResponseEntity<?> recommendCourses(
            @RequestParam String theme,
            @RequestParam Float distance,
            @RequestParam String startLocation,
            @RequestParam String endLocation,
            @RequestParam(defaultValue = "중급") String difficulty) {
        
        try {
            List<RouteData> routes = courseRecommendationService.generateOptimalRoutes(
                    theme, distance, startLocation, endLocation, difficulty);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", routes);
            response.put("count", routes.size());
            response.put("message", routes.size() + "개의 추천 코스가 생성되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * 마라톤 이벤트 생성
     * POST /api/organizer/marathons
     */
    @PostMapping
    public ResponseEntity<?> createMarathon(
            @RequestBody MarathonEventDTO dto,
            @RequestHeader(value = "X-Organizer-Id", required = false) Long organizerId) {
        
        try {
            if (organizerId == null) {
                organizerId = 1L;
            }
            
            MarathonEventDTO saved = marathonService.createMarathon(dto, organizerId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", saved);
            response.put("message", "마라톤 이벤트가 생성되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * 모든 마라톤 이벤트 조회
     * GET /api/organizer/marathons
     */
    @GetMapping
    public ResponseEntity<?> getAllMarathons() {
        try {
            List<MarathonEventDTO> marathons = marathonService.getAllMarathons();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", marathons);
            response.put("count", marathons.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "error", e.getMessage())
            );
        }
    }
    
    /**
     * 특정 마라톤 이벤트 조회
     * GET /api/organizer/marathons/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMarathon(@PathVariable Long id) {
        try {
            MarathonEventDTO marathon = marathonService.getMarathon(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", marathon);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "error", e.getMessage())
            );
        }
    }
    
    /**
     * 마라톤 이벤트 업데이트
     * PUT /api/organizer/marathons/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMarathon(
            @PathVariable Long id,
            @RequestBody MarathonEventDTO dto,
            @RequestHeader(value = "X-Organizer-Id", required = false) Long organizerId) {
        
        try {
            if (organizerId == null) {
                organizerId = 1L;
            }
            
            MarathonEventDTO updated = marathonService.updateMarathon(id, dto, organizerId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", updated);
            response.put("message", "마라톤 이벤트가 업데이트되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * 마라톤 이벤트 삭제
     * DELETE /api/organizer/marathons/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMarathon(
            @PathVariable Long id,
            @RequestHeader(value = "X-Organizer-Id", required = false) Long organizerId) {
        
        try {
            if (organizerId == null) {
                organizerId = 1L;
            }
            
            marathonService.deleteMarathon(id, organizerId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "마라톤 이벤트가 삭제되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}

