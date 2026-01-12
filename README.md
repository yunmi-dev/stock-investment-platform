# Stock Investment Platform

> 실시간 모의투자 플랫폼

## 프로젝트 소개
금융 도메인 이해를 기반으로 Redis 분산락을 활용한 동시성 제어, 2단 캐싱 전략, JVM 튜닝을 통한 성능 최적화에 집중한 모의투자 플랫폼

## 기술 스택
- **Backend**: Java 17, Spring Boot 3.2
- **Database**: PostgreSQL 15, Redis 7
- **Cache**: Redis (분산), Caffeine (로컬)
- **Infrastructure**: Docker, AWS (EC2, RDS, ElastiCache)
- **Monitoring**: Prometheus, Grafana

## 핵심 기능
- 실시간 주식 시세 조회
- 동시성 제어를 적용한 주문 처리
- 2단 캐싱 전략
- 대용량 거래 내역 조회 최적화

## 주요 문제 해결 사례

### 발생시 추가 예정

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
- Backend: Yunmi Jeong
- Frontend: Yunmi Jeong

## 라이센스
MIT
