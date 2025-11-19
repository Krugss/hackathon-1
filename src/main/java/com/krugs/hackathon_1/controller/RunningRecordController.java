package com.krugs.hackathon_1.controller;

import com.krugs.hackathon_1.dto.RunningRecordDTO;
import com.krugs.hackathon_1.service.RunningRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/runner/records")
@RequiredArgsConstructor
public class RunningRecordController {
    
    private final RunningRecordService recordService;
    
    /**
     * 새로운 달리기 기록 저장
     * POST /api/runner/records
     */
    @PostMapping
    public ResponseEntity<?> saveRecord(
            @RequestBody RunningRecordDTO dto,
            @RequestHeader(value = "X-Runner-Id", required = false) Long runnerId) {
        
        try {
            if (runnerId == null) {
                runnerId = 2L;
            }
            
            RunningRecordDTO saved = recordService.saveRecord(dto, runnerId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", saved);
            response.put("message", "기록이 저장되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * 사용자의 모든 기록 조회
     * GET /api/runner/records
     */
    @GetMapping
    public ResponseEntity<?> getRecords(
            @RequestHeader(value = "X-Runner-Id", required = false) Long runnerId) {
        
        try {
            if (runnerId == null) {
                runnerId = 2L;
            }
            
            List<RunningRecordDTO> records = recordService.getUserRecords(runnerId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", records);
            response.put("count", records.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "error", e.getMessage())
            );
        }
    }
    
    /**
     * 특정 기간의 기록 조회
     * GET /api/runner/records/period?startDate=2025-01-01T00:00:00&endDate=2025-01-31T23:59:59
     */
    @GetMapping("/period")
    public ResponseEntity<?> getRecordsByPeriod(
            @RequestHeader(value = "X-Runner-Id", required = false) Long runnerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        try {
            if (runnerId == null) {
                runnerId = 2L;
            }
            
            List<RunningRecordDTO> records = recordService.getRecordsByPeriod(runnerId, startDate, endDate);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", records);
            response.put("count", records.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "error", e.getMessage())
            );
        }
    }
    
    /**
     * 사용자 통계 조회
     * GET /api/runner/records/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getStats(
            @RequestHeader(value = "X-Runner-Id", required = false) Long runnerId) {
        
        try {
            if (runnerId == null) {
                runnerId = 2L;
            }
            
            Map<String, Object> stats = recordService.getUserStats(runnerId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", stats);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "error", e.getMessage())
            );
        }
    }
    
    /**
     * 월별 통계 조회
     * GET /api/runner/records/monthly-stats
     */
    @GetMapping("/monthly-stats")
    public ResponseEntity<?> getMonthlyStats(
            @RequestHeader(value = "X-Runner-Id", required = false) Long runnerId) {
        
        try {
            if (runnerId == null) {
                runnerId = 2L;
            }
            
            Map<String, Object> monthlyStats = recordService.getMonthlyStats(runnerId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", monthlyStats);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "error", e.getMessage())
            );
        }
    }
    
    /**
     * 특정 기록 조회
     * GET /api/runner/records/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRecord(@PathVariable Long id) {
        try {
            RunningRecordDTO record = recordService.getRecord(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", record);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "error", e.getMessage())
            );
        }
    }
    
    /**
     * 기록 삭제
     * DELETE /api/runner/records/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecord(
            @PathVariable Long id,
            @RequestHeader(value = "X-Runner-Id", required = false) Long runnerId) {
        
        try {
            if (runnerId == null) {
                runnerId = 2L;
            }
            
            recordService.deleteRecord(id, runnerId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "기록이 삭제되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "error", e.getMessage())
            );
        }
    }
}

