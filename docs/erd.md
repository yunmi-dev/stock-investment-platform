# 데이터베이스 설계 초안

## 테이블 구조

### 핵심 테이블
1. **users** - 회원
   - id, email, password, name

2. **accounts** - 계좌 (1:1 with users)
   - id, user_id, balance, total_assets
   - 초기 자본: 100만원

3. **stocks** - 종목
   - id, code, name, market (KOSPI/KOSDAQ)

4. **orders** - 주문
   - id, user_id, stock_id, order_type (BUY/SELL)
   - quantity, price, status (PENDING/COMPLETED/CANCELLED)

5. **portfolios** - 보유주식
   - id, user_id, stock_id
   - quantity, avg_purchase_price

## 관계
- users 1:1 accounts
- users 1:N orders
- users 1:N portfolios
- stocks 1:N orders
- stocks 1:N portfolios

## TODO
- [ ] 상세 ERD 작성
- [ ] 인덱스 설계 (조회 성능 위해 필요)
- [ ] 거래내역(transactions) 테이블 추가?
- [ ] 주식 시세(stock_prices) 테이블 - 시계열 데이터 어떻게 관리?
- [ ] 동시성 제어 방안 (version 컬럼?)