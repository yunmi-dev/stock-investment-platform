package com.investment.config;

import com.investment.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 핵심 설정
 *
 * 주요 설정 3가지:
 *   1. 세션 비활성화 → JWT는 서버가 상태를 안 가짐 (STATELESS)
 *   2. 엔드포인트별 접근 제어 → /api/auth/** 는 누구나, 나머지는 토큰 필요
 *   3. JwtAuthenticationFilter 등록 → 모든 요청마다 토큰 검증
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // JWT 사용 시 CSRF 불필요 (세션이 없으니까)
                .csrf(csrf -> csrf.disable())

                // 세션 안 씀 - 요청마다 토큰으로 인증
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 엔드포인트 접근 제어
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()   // 로그인/회원가입은 누구나
                        .requestMatchers("/actuator/**").permitAll()   // 헬스체크
                        .anyRequest().authenticated()                  // 나머지는 토큰 필수
                )

                // UsernamePasswordAuthenticationFilter 앞에 JWT 필터 삽입
                // → 비밀번호 검증 전에 토큰 먼저 확인
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 비밀번호 암호화
     * BCrypt: 단방향 해시 (복호화 불가, 비교만 가능)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 로그인 처리에 사용 (AuthService에서 주입받음)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
