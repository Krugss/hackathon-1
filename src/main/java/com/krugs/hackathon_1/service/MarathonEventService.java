package com.krugs.hackathon_1.service;

import com.krugs.hackathon_1.dto.MarathonEventDTO;
import com.krugs.hackathon_1.entity.MarathonEvent;
import com.krugs.hackathon_1.repository.MarathonEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarathonEventService {
    
    private final MarathonEventRepository marathonRepository;
    
    /**
     * 마라톤 이벤트 생성
     */
    public MarathonEventDTO createMarathon(MarathonEventDTO dto, Long organizerId) {
        try {
            MarathonEvent marathon = MarathonEvent.builder()
                .organizerId(organizerId)
                .eventName(dto.getEventName())
                .theme(dto.getTheme())
                .desiredDistance(dto.getDesiredDistance())
                .startLocation(dto.getStartLocation())
                .endLocation(dto.getEndLocation())
                .startLat(dto.getStartLat())
                .startLng(dto.getStartLng())
                .endLat(dto.getEndLat())
                .endLng(dto.getEndLng())
                .status("진행 중")
                .createdAt(LocalDateTime.now())
                .build();
            
            MarathonEvent saved = marathonRepository.save(marathon);
            log.info("Marathon event created. ID: {}, Organizer: {}, Event: {}", 
                     saved.getId(), organizerId, dto.getEventName());
            
            return convertToDTO(saved);
            
        } catch (Exception e) {
            log.error("Error creating marathon event", e);
            throw new RuntimeException("마라톤 이벤트 생성 실패: " + e.getMessage());
        }
    }
    
    /**
     * 모든 마라톤 이벤트 조회
     */
    public List<MarathonEventDTO> getAllMarathons() {
        List<MarathonEvent> marathons = marathonRepository.findAll();
        return marathons.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    /**
     * 특정 마라톤 이벤트 조회
     */
    public MarathonEventDTO getMarathon(Long id) {
        MarathonEvent marathon = marathonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("마라톤 이벤트를 찾을 수 없습니다."));
        return convertToDTO(marathon);
    }
    
    /**
     * 마라톤 이벤트 업데이트
     */
    public MarathonEventDTO updateMarathon(Long id, MarathonEventDTO dto, Long organizerId) {
        MarathonEvent marathon = marathonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("마라톤 이벤트를 찾을 수 없습니다."));
        
        if (!marathon.getOrganizerId().equals(organizerId)) {
            throw new RuntimeException("본인의 이벤트만 수정할 수 있습니다.");
        }
        
        marathon.setEventName(dto.getEventName());
        marathon.setTheme(dto.getTheme());
        marathon.setDesiredDistance(dto.getDesiredDistance());
        marathon.setStartLocation(dto.getStartLocation());
        marathon.setEndLocation(dto.getEndLocation());
        marathon.setStartLat(dto.getStartLat());
        marathon.setStartLng(dto.getStartLng());
        marathon.setEndLat(dto.getEndLat());
        marathon.setEndLng(dto.getEndLng());
        marathon.setStatus(dto.getStatus());
        
        MarathonEvent updated = marathonRepository.save(marathon);
        log.info("Marathon event updated. ID: {}", id);
        
        return convertToDTO(updated);
    }
    
    /**
     * 마라톤 이벤트 삭제
     */
    public void deleteMarathon(Long id, Long organizerId) {
        MarathonEvent marathon = marathonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("마라톤 이벤트를 찾을 수 없습니다."));
        
        if (!marathon.getOrganizerId().equals(organizerId)) {
            throw new RuntimeException("본인의 이벤트만 삭제할 수 있습니다.");
        }
        
        marathonRepository.deleteById(id);
        log.info("Marathon event deleted. ID: {}", id);
    }
    
    /**
     * Entity를 DTO로 변환
     */
    private MarathonEventDTO convertToDTO(MarathonEvent marathon) {
        return MarathonEventDTO.builder()
            .id(marathon.getId())
            .organizerId(marathon.getOrganizerId())
            .eventName(marathon.getEventName())
            .theme(marathon.getTheme())
            .desiredDistance(marathon.getDesiredDistance())
            .startLocation(marathon.getStartLocation())
            .endLocation(marathon.getEndLocation())
            .startLat(marathon.getStartLat())
            .startLng(marathon.getStartLng())
            .endLat(marathon.getEndLat())
            .endLng(marathon.getEndLng())
            .status(marathon.getStatus())
            .createdAt(marathon.getCreatedAt())
            .build();
    }
}

