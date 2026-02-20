package com.investment.service;

import com.investment.domain.user.Account;
import com.investment.domain.user.User;
import com.investment.dto.request.LoginRequest;
import com.investment.dto.request.RegisterRequest;
import com.investment.dto.response.AuthResponse;
import com.investment.repository.AccountRepository;
import com.investment.repository.UserRepository;
import com.investment.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    /**
     * 회원가입
     * 1. 이메일 중복 체크
     * 2. 비밀번호 BCrypt 암호화 후 저장
     * 3. 초기 계좌 자동 생성 (100만원)
     * 4. JWT 토큰 발급
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phone(request.getPhone())
                .build();
        userRepository.save(user);

        // 회원가입 시 계좌 자동 생성 (초기 잔고 100만원)
        Account account = Account.builder()
                .user(user)
                .build();
        accountRepository.save(account);

        String token = jwtTokenProvider.generateToken(user.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getName());
    }

    /**
     * 로그인
     * 1. AuthenticationManager가 이메일/비밀번호 검증
     *    → 내부에서 CustomUserDetailsService.loadUserByUsername() 호출
     *    → BCrypt로 비밀번호 비교
     * 2. 검증 성공 시 JWT 토큰 발급
     */
    public AuthResponse login(LoginRequest request) {
        // 비밀번호 틀리면 여기서 BadCredentialsException 발생 (401 응답)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String token = jwtTokenProvider.generateToken(user.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getName());
    }
}
