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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security 핵심 설정
 *
 * 주요 설정:
 *   1. 세션 비활성화 → JWT는 서버가 상태를 안 가짐 (STATELESS)
 *   2. CORS 허용 → React 개발 서버(3000) ↔ Spring(8080) 통신
 *   3. 엔드포인트별 접근 제어 → /api/auth/** 는 누구나, 나머지는 토큰 필요
 *   4. JwtAuthenticationFilter 등록 → 모든 요청마다 토큰 검증
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

                // CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

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
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS 설정
     * React 개발 서버(localhost:3000)에서 오는 요청 허용
     * 배포 시 실제 도메인으로 변경 필요
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:3000"   // React 개발 서버
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
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
