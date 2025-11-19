package com.krugs.hackathon_1.repository;

import com.krugs.hackathon_1.entity.MarathonEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MarathonEventRepository extends JpaRepository<MarathonEvent, Long> {
    
    List<MarathonEvent> findByOrganizerIdOrderByCreatedAtDesc(Long organizerId);
    
    List<MarathonEvent> findByStatusOrderByCreatedAtDesc(String status);
    
    List<MarathonEvent> findByThemeOrderByCreatedAtDesc(String theme);
}

