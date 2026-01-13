# API 설계 초안

## 인증
- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/login` - 로그인
- `POST /api/auth/logout` - 로그아웃

## 주식
- `GET /api/stocks` - 주식 목록
- `GET /api/stocks/{id}` - 주식 상세
- `GET /api/stocks/{id}/price` - 시세 조회

## 주문
- `POST /api/orders` - 주문 생성
- `GET /api/orders` - 주문 내역
- `DELETE /api/orders/{id}` - 주문 취소

## 포트폴리오
- `GET /api/portfolio` - 보유 주식

## 응답 형식

### 성공
```json
{
  "success": true,
  "data": {},
  "message": "Success"
}
```

### 실패
```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "에러 메시지"
  }
}
```

## TODO
- [ ] 상세 요청/응답 스펙 작성
- [ ] 에러 코드 정의
- [ ] JWT 인증 방식 구체화