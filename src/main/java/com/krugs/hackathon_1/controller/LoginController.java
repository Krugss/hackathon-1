package com.krugs.hackathon_1.controller;

import com.krugs.hackathon_1.dto.LoginRequest;
import com.krugs.hackathon_1.dto.LoginResponse;
import com.krugs.hackathon_1.dto.SignUpRequest;
import com.krugs.hackathon_1.dto.SignUpResponse;
import com.krugs.hackathon_1.service.LoginService;
import com.krugs.hackathon_1.service.LocalAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {
    
    private final LoginService loginService;
    private final LocalAuthService localAuthService;
    
    /**
     * 회원가입 (로컬)
     */
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        SignUpResponse response = localAuthService.localSignUp(signUpRequest);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 로그인 (로컬 - JWT 토큰 생성)
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = localAuthService.localLogin(loginRequest);
        
        if (response.getAccessToken() != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 토큰 검증
     */
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        try {
            String actualToken = token.replace("Bearer ", "");
            boolean isValid = localAuthService.validateToken(actualToken);
            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
}

