-- 테스트 회원
INSERT IGNORE INTO users (email, password, name, phone, created_at, updated_at)
VALUES ('test@test.com', 'password123', '홍길동', '010-1234-5678', NOW(), NOW());

-- 테스트 계좌 (user_id=1, 초기 자본 100만원)
INSERT IGNORE INTO accounts (user_id, balance, total_assets, total_profit_loss, profit_loss_rate, version, updated_at)
VALUES (1, 1000000.00, 1000000.00, 0.00, 0.0000, 0, NOW());

-- 주식 종목
INSERT IGNORE INTO stocks (code, name, market, current_price, previous_close, volume, market_cap, is_active, last_updated, created_at)
VALUES
    ('005930', '삼성전자', 'KOSPI', 71000.00, 70500.00, 15000000, 4230000.00, true, NOW(), NOW()),
    ('035720', '카카오', 'KOSPI', 45000.00, 44800.00, 8000000, 189000.00, true, NOW(), NOW()),
    ('000660', 'SK하이닉스', 'KOSPI', 128000.00, 127500.00, 5000000, 932000.00, true, NOW(), NOW()),
    ('035420', 'NAVER', 'KOSPI', 195000.00, 194000.00, 3500000, 320000.00, true, NOW(), NOW()),
    ('051910', 'LG화학', 'KOSPI', 420000.00, 418000.00, 2000000, 296000.00, true, NOW(), NOW());
