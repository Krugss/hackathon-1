package com.krugs.hackathon_1.service;

import com.krugs.hackathon_1.dto.LoginRequest;
import com.krugs.hackathon_1.dto.LoginResponse;
import com.krugs.hackathon_1.dto.SignUpRequest;
import com.krugs.hackathon_1.dto.SignUpResponse;
import com.krugs.hackathon_1.entity.User;
import com.krugs.hackathon_1.repository.UserRepository;
import com.krugs.hackathon_1.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalAuthService {
    
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 로컬 로그인 (JWT 토큰 생성)
     */
    public LoginResponse localLogin(LoginRequest loginRequest) {
        try {
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new RuntimeException("비밀번호가 일치하지 않습니다.");
            }
            
            String token = jwtUtil.generateToken(user.getUsername(), user.getId());
            
            return LoginResponse.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .expiresIn(3600L)
                    .message("로그인 성공")
                    .userId(user.getId())
                    .username(user.getUsername())
                    .build();
                    
        } catch (Exception e) {
            return LoginResponse.builder()
                    .message("로그인 실패: " + e.getMessage())
                    .build();
        }
    }
    
    /**
     * 로컬 회원가입
     */
    public SignUpResponse localSignUp(SignUpRequest signUpRequest) {
        try {
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                throw new RuntimeException("이미 존재하는 사용자명입니다.");
            }
            
            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                throw new RuntimeException("이미 존재하는 이메일입니다.");
            }
            
            User user = User.builder()
                    .username(signUpRequest.getUsername())
                    .password(passwordEncoder.encode(signUpRequest.getPassword()))
                    .email(signUpRequest.getEmail())
                    .build();
            
            userRepository.save(user);
            
            return SignUpResponse.builder()
                    .message("회원가입이 완료되었습니다.")
                    .username(user.getUsername())
                    .success(true)
                    .build();
                    
        } catch (Exception e) {
            return SignUpResponse.builder()
                    .message("회원가입 실패: " + e.getMessage())
                    .success(false)
                    .build();
        }
    }
    
    /**
     * 토큰 검증
     */
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}

