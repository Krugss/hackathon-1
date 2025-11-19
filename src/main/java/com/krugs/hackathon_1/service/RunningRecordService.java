package com.krugs.hackathon_1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krugs.hackathon_1.dto.RunningRecordDTO;
import com.krugs.hackathon_1.entity.RunningRecord;
import com.krugs.hackathon_1.repository.RunningRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RunningRecordService {
    
    private final RunningRecordRepository recordRepository;
    private final ObjectMapper objectMapper;
    
    /**
     * 달리기 기록 저장
     */
    public RunningRecordDTO saveRecord(RunningRecordDTO dto, Long runnerId) {
        try {
            String gpsJson = null;
            if (dto.getGpsData() != null) {
                gpsJson = objectMapper.writeValueAsString(dto.getGpsData());
            }
            
            Float avgPace = calculateAvgPace(dto.getDistance().doubleValue(), dto.getDurationSeconds().doubleValue());
            Integer calories = dto.getCaloriesBurned() != null ? 
                dto.getCaloriesBurned() : calculateCalories(dto.getDistance());
            
            RunningRecord record = RunningRecord.builder()
                .runnerId(runnerId)
                .courseId(dto.getCourseId())
                .distance(dto.getDistance())
                .durationSeconds(dto.getDurationSeconds())
                .avgPace(avgPace)
                .caloriesBurned(calories)
                .routePolyline(dto.getRoutePolyline())
                .gpsData(gpsJson)
                .notes(dto.getNotes())
                .recordDate(dto.getRecordDate() != null ? dto.getRecordDate() : LocalDateTime.now())
                .build();
            
            RunningRecord saved = recordRepository.save(record);
            log.info("Running record saved. ID: {}, Runner: {}, Distance: {}km", 
                     saved.getId(), runnerId, dto.getDistance());
            
            return convertToDTO(saved);
            
        } catch (Exception e) {
            log.error("Error saving running record", e);
            throw new RuntimeException("기록 저장 실패: " + e.getMessage());
        }
    }
    
    /**
     * 사용자의 모든 기록 조회
     */
    public List<RunningRecordDTO> getUserRecords(Long runnerId) {
        List<RunningRecord> records = recordRepository.findByRunnerIdOrderByRecordDateDesc(runnerId);
        return records.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    /**
     * 특정 기간의 기록 조회
     */
    public List<RunningRecordDTO> getRecordsByPeriod(Long runnerId, LocalDateTime startDate, LocalDateTime endDate) {
        List<RunningRecord> records = recordRepository
            .findByRunnerIdAndRecordDateBetweenOrderByRecordDateDesc(runnerId, startDate, endDate);
        return records.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    /**
     * 사용자 통계 조회
     */
    public Map<String, Object> getUserStats(Long runnerId) {
        List<RunningRecord> allRecords = recordRepository.findByRunnerIdOrderByRecordDateDesc(runnerId);
        
        Map<String, Object> stats = new HashMap<>();
        
        if (allRecords.isEmpty()) {
            stats.put("totalRecords", 0);
            stats.put("totalDistance", 0.0);
            stats.put("totalTime", 0);
            stats.put("totalCalories", 0);
            stats.put("avgPace", 0.0);
            stats.put("avgDistance", 0.0);
            return stats;
        }
        
        double totalDistance = allRecords.stream()
            .mapToDouble(RunningRecord::getDistance)
            .sum();
        
        long totalTime = allRecords.stream()
            .mapToLong(RunningRecord::getDurationSeconds)
            .sum();
        
        int totalCalories = allRecords.stream()
            .mapToInt(r -> r.getCaloriesBurned() != null ? r.getCaloriesBurned() : 0)
            .sum();
        
        float avgPace = calculateAvgPace(totalDistance, (double)totalTime);
        
        stats.put("totalRecords", allRecords.size());
        stats.put("totalDistance", Math.round(totalDistance * 100.0) / 100.0);
        stats.put("totalTime", totalTime);
        stats.put("totalCalories", totalCalories);
        stats.put("avgPace", Math.round(avgPace * 100.0) / 100.0);
        stats.put("avgDistance", Math.round((totalDistance / allRecords.size()) * 100.0) / 100.0);
        
        return stats;
    }
    
    /**
     * 월별 통계
     */
    public Map<String, Object> getMonthlyStats(Long runnerId) {
        List<RunningRecord> records = recordRepository.findByRunnerIdOrderByRecordDateDesc(runnerId);
        
        Map<String, Object> monthlyStats = new LinkedHashMap<>();
        
        records.stream()
            .collect(Collectors.groupingBy(
                r -> YearMonth.from(r.getRecordDate()),
                Collectors.summingDouble(RunningRecord::getDistance)
            ))
            .entrySet()
            .stream()
            .sorted((a, b) -> b.getKey().compareTo(a.getKey()))
            .forEach(entry -> monthlyStats.put(
                entry.getKey().toString(),
                Math.round(entry.getValue() * 100.0) / 100.0
            ));
        
        return monthlyStats;
    }
    
    /**
     * 특정 기록 조회
     */
    public RunningRecordDTO getRecord(Long recordId) {
        RunningRecord record = recordRepository.findById(recordId)
            .orElseThrow(() -> new RuntimeException("기록을 찾을 수 없습니다."));
        return convertToDTO(record);
    }
    
    /**
     * 기록 삭제
     */
    public void deleteRecord(Long recordId, Long runnerId) {
        RunningRecord record = recordRepository.findById(recordId)
            .orElseThrow(() -> new RuntimeException("기록을 찾을 수 없습니다."));
        
        if (!record.getRunnerId().equals(runnerId)) {
            throw new RuntimeException("본인의 기록만 삭제할 수 있습니다.");
        }
        
        recordRepository.deleteById(recordId);
        log.info("Running record deleted. ID: {}", recordId);
    }
    
    /**
     * 평균 페이스 계산 (분/km)
     */
    private Float calculateAvgPace(Double distance, Double durationSeconds) {
        if (distance == null || distance == 0 || durationSeconds == null) {
            return 0f;
        }
        return (float) (durationSeconds / 60.0) / distance.floatValue();
    }
    
    /**
     * 소모 칼로리 계산
     */
    private Integer calculateCalories(Float distance) {
        if (distance == null || distance == 0) {
            return 0;
        }
        double weight = 70.0;
        return (int) (weight * distance * 1.036);
    }
    
    /**
     * Entity를 DTO로 변환
     */
    private RunningRecordDTO convertToDTO(RunningRecord record) {
        return RunningRecordDTO.builder()
            .id(record.getId())
            .runnerId(record.getRunnerId())
            .courseId(record.getCourseId())
            .distance(record.getDistance())
            .durationSeconds(record.getDurationSeconds())
            .avgPace(record.getAvgPace())
            .caloriesBurned(record.getCaloriesBurned())
            .routePolyline(record.getRoutePolyline())
            .notes(record.getNotes())
            .recordDate(record.getRecordDate())
            .createdAt(record.getCreatedAt())
            .build();
    }
}

