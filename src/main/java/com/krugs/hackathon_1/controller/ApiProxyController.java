package com.krugs.hackathon_1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiProxyController {

    @Value("${auth.server.url}")
    private String authServerUrl;

    private final RestTemplate restTemplate;

    /**
     * Auth 서버의 /api/auth/* 요청을 프록시
     * 프론트엔드: localhost:8080/api/auth/token
     * → 백엔드: localhost:8081/api/auth/token
     */
    @PostMapping("/token")
    public ResponseEntity<?> login(@RequestBody Object body, @RequestHeader HttpHeaders headers) {
        String url = authServerUrl + "/api/auth/token";
        return restTemplate.postForEntity(url, body, Object.class);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Object body, @RequestHeader HttpHeaders headers) {
        String url = authServerUrl + "/api/auth/signup";
        return restTemplate.postForEntity(url, body, Object.class);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String token) {
        String url = authServerUrl + "/api/auth/validate";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
    }
}

