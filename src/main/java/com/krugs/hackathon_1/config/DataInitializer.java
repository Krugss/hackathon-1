package com.krugs.hackathon_1.config;

import com.krugs.hackathon_1.entity.User;
import com.krugs.hackathon_1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    
    private final PasswordEncoder passwordEncoder;
    
    @Bean
    public CommandLineRunner initializeData(UserRepository userRepository) {
        return args -> {
            // 테이블이 비어있으면 초기 데이터 삽입
            if (userRepository.count() == 0) {
                // 비밀번호: password123
                String encodedPassword = passwordEncoder.encode("password123");
                
                User testUser = User.builder()
                        .username("testuser")
                        .password(encodedPassword)
                        .email("test@example.com")
                        .build();
                
                User organizer = User.builder()
                        .username("organizer")
                        .password(encodedPassword)
                        .email("organizer@example.com")
                        .build();
                
                User runner = User.builder()
                        .username("runner")
                        .password(encodedPassword)
                        .email("runner@example.com")
                        .build();
                
                userRepository.save(testUser);
                userRepository.save(organizer);
                userRepository.save(runner);
                
                System.out.println("✅ 초기 사용자 데이터가 생성되었습니다!");
                System.out.println("테스트 계정:");
                System.out.println("  - testuser / password123");
                System.out.println("  - organizer / password123");
                System.out.println("  - runner / password123");
            }
        };
    }
}

