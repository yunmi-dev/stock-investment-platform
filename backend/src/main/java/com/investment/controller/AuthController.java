package com.investment.controller;

import com.investment.dto.request.LoginRequest;
import com.investment.dto.request.RegisterRequest;
import com.investment.dto.response.ApiResponse;
import com.investment.dto.response.AuthResponse;
import com.investment.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 API
 *
 * POST /api/auth/register  → 회원가입
 * POST /api/auth/login     → 로그인
 *
 * SecurityConfig에서 /api/auth/** 는 permitAll() 처리했으므로
 * 토큰 없이 호출 가능
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
