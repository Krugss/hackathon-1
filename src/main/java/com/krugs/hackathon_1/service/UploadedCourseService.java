package com.krugs.hackathon_1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krugs.hackathon_1.dto.UploadedCourseDTO;
import com.krugs.hackathon_1.entity.UploadedCourse;
import com.krugs.hackathon_1.repository.UploadedCourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadedCourseService {
    
    private final UploadedCourseRepository courseRepository;
    private final ObjectMapper objectMapper;
    
    /**
     * 코스 업로드
     */
    public UploadedCourseDTO uploadCourse(UploadedCourseDTO dto, Long runnerId) {
        try {
            // ✅ 좌표 데이터를 JSON으로 변환하여 저장
            String coordinatesJson = "";
            String elevationDataJson = "";
            
            if (dto.getCoordinates() != null && !dto.getCoordinates().isEmpty()) {
                coordinatesJson = objectMapper.writeValueAsString(dto.getCoordinates());
                log.info("Coordinates saved: {} items", dto.getCoordinates().size());
            }
            
            if (dto.getElevationData() != null && !dto.getElevationData().isEmpty()) {
                elevationDataJson = objectMapper.writeValueAsString(dto.getElevationData());
                log.info("Elevation data saved: {} items", dto.getElevationData().size());
            }
            
            UploadedCourse course = UploadedCourse.builder()
                .runnerId(runnerId)
                .courseName(dto.getCourseName())
                .theme(dto.getTheme())
                .distance(dto.getDistance())
                .difficultyLevel(dto.getDifficultyLevel())
                .routePolyline(dto.getRoutePolyline())
                .gpxFileUrl(dto.getGpxFileUrl())
                .description(dto.getDescription())
                .coordinatesJson(coordinatesJson)
                .elevationDataJson(elevationDataJson)
                .build();
            
            UploadedCourse saved = courseRepository.save(course);
            log.info("Course uploaded. ID: {}, Runner: {}, Name: {}", 
                     saved.getId(), runnerId, dto.getCourseName());
            
            return convertToDTO(saved);
            
        } catch (Exception e) {
            log.error("Error uploading course", e);
            throw new RuntimeException("코스 업로드 실패: " + e.getMessage());
        }
    }
    
    /**
     * 사용자의 코스 조회
     */
    public List<UploadedCourseDTO> getUserCourses(Long runnerId) {
        List<UploadedCourse> courses = courseRepository.findByRunnerIdOrderByCreatedAtDesc(runnerId);
        return courses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    /**
     * 테마별 코스 조회 (인기순)
     */
    public List<UploadedCourseDTO> getCoursesByTheme(String theme) {
        List<UploadedCourse> courses = courseRepository.findByThemeOrderByLikeCountDesc(theme);
        return courses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    /**
     * 난이도별 코스 조회
     */
    public List<UploadedCourseDTO> getCoursesByDifficulty(String difficultyLevel) {
        List<UploadedCourse> courses = courseRepository.findByDifficultyLevelOrderByCreatedAtDesc(difficultyLevel);
        return courses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    /**
     * 코스 검색 (테마 + 난이도 필터)
     */
    public List<UploadedCourseDTO> searchCourses(String theme, String difficultyLevel) {
        List<UploadedCourse> courses = courseRepository.searchCourses(theme, difficultyLevel);
        return courses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    /**
     * 특정 코스 조회
     */
    public UploadedCourseDTO getCourse(Long courseId) {
        UploadedCourse course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("코스를 찾을 수 없습니다."));
        
        // 조회수 증가
        course.setViewCount((course.getViewCount() != null ? course.getViewCount() : 0) + 1);
        courseRepository.save(course);
        
        return convertToDTO(course);
    }
    
    /**
     * 코스 정보 수정
     */
    public UploadedCourseDTO updateCourse(Long courseId, UploadedCourseDTO dto, Long runnerId) {
        UploadedCourse course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("코스를 찾을 수 없습니다."));
        
        if (!course.getRunnerId().equals(runnerId)) {
            throw new RuntimeException("본인의 코스만 수정할 수 있습니다.");
        }
        
        if (dto.getCourseName() != null) {
            course.setCourseName(dto.getCourseName());
        }
        if (dto.getDescription() != null) {
            course.setDescription(dto.getDescription());
        }
        if (dto.getTheme() != null) {
            course.setTheme(dto.getTheme());
        }
        if (dto.getDifficultyLevel() != null) {
            course.setDifficultyLevel(dto.getDifficultyLevel());
        }
        
        UploadedCourse updated = courseRepository.save(course);
        log.info("Course updated. ID: {}", courseId);
        
        return convertToDTO(updated);
    }
    
    /**
     * 코스 삭제
     */
    public void deleteCourse(Long courseId, Long runnerId) {
        UploadedCourse course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("코스를 찾을 수 없습니다."));
        
        if (!course.getRunnerId().equals(runnerId)) {
            throw new RuntimeException("본인의 코스만 삭제할 수 있습니다.");
        }
        
        courseRepository.deleteById(courseId);
        log.info("Course deleted. ID: {}", courseId);
    }
    
    /**
     * 인기 코스 상위 5개 (테마별)
     */
    public List<UploadedCourseDTO> getTopCoursesByTheme(String theme, int limit) {
        List<UploadedCourse> courses = courseRepository.findByThemeOrderByLikeCountDesc(theme);
        return courses.stream()
            .limit(limit)
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Entity를 DTO로 변환 (JSON 데이터 역변환 포함)
     */
    private UploadedCourseDTO convertToDTO(UploadedCourse course) {
        try {
            // ✅ 좌표 데이터 역변환
            List<Map<String, Double>> coordinates = null;
            if (course.getCoordinatesJson() != null && !course.getCoordinatesJson().isEmpty()) {
                coordinates = objectMapper.readValue(course.getCoordinatesJson(), 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
                log.info("Coordinates loaded: {} items", coordinates.size());
            }
            
            // ✅ 고도 데이터 역변환
            List<Double> elevationData = null;
            if (course.getElevationDataJson() != null && !course.getElevationDataJson().isEmpty()) {
                elevationData = objectMapper.readValue(course.getElevationDataJson(), 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Double.class));
                log.info("Elevation data loaded: {} items", elevationData.size());
            }
            
            return UploadedCourseDTO.builder()
                .id(course.getId())
                .runnerId(course.getRunnerId())
                .courseName(course.getCourseName())
                .theme(course.getTheme())
                .distance(course.getDistance())
                .difficultyLevel(course.getDifficultyLevel())
                .routePolyline(course.getRoutePolyline())
                .gpxFileUrl(course.getGpxFileUrl())
                .description(course.getDescription())
                .likeCount(course.getLikeCount())
                .viewCount(course.getViewCount())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .coordinates(coordinates)
                .elevationData(elevationData)
                .build();
        } catch (Exception e) {
            log.error("Error converting course to DTO", e);
            // ✅ 오류 발생 시에도 기본 정보는 반환
            return UploadedCourseDTO.builder()
                .id(course.getId())
                .runnerId(course.getRunnerId())
                .courseName(course.getCourseName())
                .theme(course.getTheme())
                .distance(course.getDistance())
                .difficultyLevel(course.getDifficultyLevel())
                .description(course.getDescription())
                .likeCount(course.getLikeCount())
                .viewCount(course.getViewCount())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
        }
    }
}

