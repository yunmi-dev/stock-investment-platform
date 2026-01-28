package com.investment.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 회원 엔티티
 * users 테이블과 매핑됨
 */
@Entity  // 1. JPA에게 테이블임을 알림
@Table(name = "users")  // 2. 테이블 이름 명시 (선택사항, 없으면 클래스명이 테이블명)
@Getter  // 3. Lombok으로 모든 필드의 getter 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 4. 기본 생성자 (JPA 필수)
@EntityListeners(AuditingEntityListener.class)  // 5. 생성/수정 시간 자동 관리
public class User {

    @Id  // 6. Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 7. Auto Increment
    private Long id;

    @Column(nullable = false, unique = true, length = 255)  // 8. 컬럼 설정
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 20)
    private String phone;

    @CreatedDate  // 9. 생성 시간 자동 입력
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate  // 10. 수정 시간 자동 업데이트
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 11. Builder 패턴 (객체 생성 방법)
    @Builder
    public User(String email, String password, String name, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    // 12. 비즈니스 로직 (프로필 수정)
    public void updateProfile(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}