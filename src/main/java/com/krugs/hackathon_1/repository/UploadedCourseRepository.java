package com.krugs.hackathon_1.repository;

import com.krugs.hackathon_1.entity.UploadedCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UploadedCourseRepository extends JpaRepository<UploadedCourse, Long> {
    
    List<UploadedCourse> findByRunnerIdOrderByCreatedAtDesc(Long runnerId);
    
    List<UploadedCourse> findByThemeOrderByLikeCountDesc(String theme);
    
    List<UploadedCourse> findByDifficultyLevelOrderByCreatedAtDesc(String difficultyLevel);
    
    @Query("SELECT c FROM UploadedCourse c WHERE " +
           "(:theme IS NULL OR c.theme = :theme) AND " +
           "(:difficultyLevel IS NULL OR c.difficultyLevel = :difficultyLevel) " +
           "ORDER BY c.likeCount DESC")
    List<UploadedCourse> searchCourses(
        @Param("theme") String theme,
        @Param("difficultyLevel") String difficultyLevel
    );
}

