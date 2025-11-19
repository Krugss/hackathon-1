package com.krugs.hackathon_1.repository;

import com.krugs.hackathon_1.entity.RunningRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RunningRecordRepository extends JpaRepository<RunningRecord, Long> {
    
    List<RunningRecord> findByRunnerIdOrderByRecordDateDesc(Long runnerId);
    
    List<RunningRecord> findByRunnerIdAndRecordDateBetweenOrderByRecordDateDesc(
        Long runnerId,
        LocalDateTime startDate,
        LocalDateTime endDate
    );
    
    List<RunningRecord> findByCourseIdOrderByRecordDateDesc(Long courseId);
}

