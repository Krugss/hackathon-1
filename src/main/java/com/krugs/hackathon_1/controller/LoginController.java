package com.krugs.hackathon_1.controller;

import com.krugs.hackathon_1.dto.LoginRequest;
import com.krugs.hackathon_1.dto.LoginResponse;
import com.krugs.hackathon_1.dto.SignUpRequest;
import com.krugs.hackathon_1.dto.SignUpResponse;
import com.krugs.hackathon_1.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {
    
    private final LoginService loginService;
    
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        SignUpResponse response = loginService.signUp(signUpRequest);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = loginService.login(loginRequest);
        
        if (response.getAccessToken() != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}

