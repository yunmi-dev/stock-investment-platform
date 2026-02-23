# Stock Investment Platform

> 실시간으로 가격 반영한 모의투자 플랫폼

## 프로젝트 소개

금융 도메인 이해를 기반으로 **Redis 분산락을 활용한 동시성 제어**, **JWT 인증**, **글로벌 예외 처리** 등 실무 패턴에 집중한 모의투자 플랫폼.

React 프론트엔드 + Spring Boot 백엔드 풀스택 구성으로, 실제 주식 매수/매도 흐름을 시뮬레이션한다.

---

## 기술 스택

| 영역 | 기술 |
|------|------|
| Backend | Java 17, Spring Boot 3.2.1 |
| Security | Spring Security, JWT (jjwt 0.12.3) |
| Database | MySQL 8.0 |
| Cache / Lock | Redis 7, Redisson (분산락) |
| ORM | Spring Data JPA, Hibernate |
| Frontend | React 18, React Router v6, Axios, Vite |
| Infra | Docker, Docker Compose |

---

## 핵심 기능
- 실시간 주식 시세 조회
- 동시성 제어를 적용한 주문 처리
- 2단 캐싱 전략
- 대용량 거래 내역 조회 최적화

---

## 구현 완료 목록

- [x] JWT 인증 - 회원가입, 로그인, 토큰 발급
- [x] Spring Security - /api/auth/** 공개, 나머지 토큰 인증 필요
- [x] 주식 조회 - GET /api/stocks (전체), GET /api/stocks/{code}
- [x] 주문 처리 - 매수/매도 (잔고 확인, 포트폴리오 업데이트)
- [x] Redis 분산락 - @DistributedLock 동시 주문 동시성 제어
- [x] 글로벌 예외 처리 - GlobalExceptionHandler + ApiResponse
- [x] MySQL 전환 - PostgreSQL → MySQL 8.0 전체 반영
- [x] CORS - localhost:3000 허용
- [x] React 프론트엔드 - 로그인/회원가입/대시보드
- [x] 초기 데이터 - 종목 5개 + 테스트 계좌 100만원

---

## 주요 문제 해결 사례

### 발생시 추가 예정

---

## 아키텍처
![Architecture](docs/images/architecture.png)

## ERD
![ERD](docs/images/erd.png)

## API 문서
[Swagger UI](http://localhost:8080/swagger-ui.html)

## 실행 방법
```bash
# Docker Compose로 로컬 실행
docker-compose up -d

# 애플리케이션 실행
./gradlew bootRun
```

## 성능 테스트 결과
- 테스트후 추가 예정

## 개발 기간
2025.01 ~ 2025.03

## 개발자
- Yunmi Jeong

## 라이센스
MIT
