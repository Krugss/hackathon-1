package com.krugs.hackathon_1.service;

import com.krugs.hackathon_1.dto.LoginRequest;
import com.krugs.hackathon_1.dto.LoginResponse;
import com.krugs.hackathon_1.dto.SignUpRequest;
import com.krugs.hackathon_1.dto.SignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {
    
    private final WebClient authWebClient;
    
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Map<String, String> tokenResponse = authWebClient.post()
                    .uri("/api/auth/token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(loginRequest)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            
            if (tokenResponse != null) {
                return LoginResponse.builder()
                        .accessToken(tokenResponse.get("accessToken"))
                        .tokenType(tokenResponse.get("tokenType"))
                        .expiresIn(Long.valueOf(tokenResponse.get("expiresIn").toString()))
                        .message("로그인 성공")
                        .build();
            } else {
                return LoginResponse.builder()
                        .message("로그인 실패: 인증 서버 응답 없음")
                        .build();
            }
        } catch (Exception e) {
            return LoginResponse.builder()
                    .message("로그인 실패: " + e.getMessage())
                    .build();
        }
    }
    
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        try {
            SignUpResponse response = authWebClient.post()
                    .uri("/api/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(signUpRequest)
                    .retrieve()
                    .bodyToMono(SignUpResponse.class)
                    .block();
            
            if (response != null && response.isSuccess()) {
                return response;
            } else {
                return SignUpResponse.builder()
                        .message(response != null ? response.getMessage() : "회원가입 실패: 인증 서버 응답 없음")
                        .success(false)
                        .build();
            }
        } catch (Exception e) {
            return SignUpResponse.builder()
                    .message("회원가입 실패: " + e.getMessage())
                    .success(false)
                    .build();
        }
    }
}

