package com.krugs.hackathon_1.controller;

import com.krugs.hackathon_1.dto.UploadedCourseDTO;
import com.krugs.hackathon_1.service.UploadedCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/runner/courses")
@RequiredArgsConstructor
public class UploadedCourseController {
    
    private final UploadedCourseService courseService;
    
    /**
     * 코스 업로드
     * POST /api/runner/courses
     */
    @PostMapping
    public ResponseEntity<?> uploadCourse(
            @RequestBody UploadedCourseDTO dto,
            @RequestHeader(value = "X-Runner-Id", required = false) Long runnerId) {
        
        try {
            if (runnerId == null) {
                runnerId = 2L;
            }
            
            UploadedCourseDTO saved = courseService.uploadCourse(dto, runnerId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", saved);
            response.put("message", "코스가 업로드되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * 사용자의 모든 코스 조회
     * GET /api/runner/courses/my
     */
    @GetMapping("/my")
    public ResponseEntity<?> getUserCourses(
            @RequestHeader(value = "X-Runner-Id", required = false) Long runnerId) {
        
        try {
            if (runnerId == null) {
                runnerId = 2L;
            }
            
            List<UploadedCourseDTO> courses = courseService.getUserCourses(runnerId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", courses);
            response.put("count", courses.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "error", e.getMessage())
            );
        }
    }
    
    /**
     * 모든 코스 조회 (검색)
     * GET /api/runner/courses?theme=자연경관&difficulty=intermediate
     */
    @GetMapping
    public ResponseEntity<?> searchCourses(
            @RequestParam(required = false) String theme,
            @RequestParam(required = false, name = "difficulty") String difficultyLevel) {
        
        try {
            List<UploadedCourseDTO> courses;
            
            if (theme != null || difficultyLevel != null) {
                courses = courseService.searchCourses(theme, difficultyLevel);
            } else {
                courses = courseService.searchCourses(null, null);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", courses);
            response.put("count", courses.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "error", e.getMessage())
            );
        }
    }
    
    /**
     * 테마별 인기 코스 상위 5개
     * GET /api/runner/courses/theme/{theme}/top
     */
    @GetMapping("/theme/{theme}/top")
    public ResponseEntity<?> getTopCoursesByTheme(
            @PathVariable String theme,
            @RequestParam(defaultValue = "5") int limit) {
        
        try {
            List<UploadedCourseDTO> courses = courseService.getTopCoursesByTheme(theme, limit);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", courses);
            response.put("count", courses.size());
            response.put("theme", theme);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "error", e.getMessage())
            );
        }
    }
    
    /**
     * 특정 코스 조회
     * GET /api/runner/courses/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(@PathVariable Long id) {
        try {
            UploadedCourseDTO course = courseService.getCourse(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", course);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "error", e.getMessage())
            );
        }
    }
    
    /**
     * 코스 정보 수정
     * PUT /api/runner/courses/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(
            @PathVariable Long id,
            @RequestBody UploadedCourseDTO dto,
            @RequestHeader(value = "X-Runner-Id", required = false) Long runnerId) {
        
        try {
            if (runnerId == null) {
                runnerId = 2L;
            }
            
            UploadedCourseDTO updated = courseService.updateCourse(id, dto, runnerId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", updated);
            response.put("message", "코스가 수정되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "error", e.getMessage())
            );
        }
    }
    
    /**
     * 코스 삭제
     * DELETE /api/runner/courses/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(
            @PathVariable Long id,
            @RequestHeader(value = "X-Runner-Id", required = false) Long runnerId) {
        
        try {
            if (runnerId == null) {
                runnerId = 2L;
            }
            
            courseService.deleteCourse(id, runnerId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "코스가 삭제되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "error", e.getMessage())
            );
        }
    }
}

