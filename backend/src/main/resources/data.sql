-- 테스트 회원
INSERT INTO users (email, password, name, phone, created_at, updated_at)
VALUES ('test@test.com', 'password123', '홍길동', '010-1234-5678', NOW(), NOW())
ON CONFLICT (email) DO NOTHING;

-- 테스트 계좌 (user_id=1, 초기 자본 100만원)
INSERT INTO accounts (user_id, balance, total_assets, total_profit_loss, profit_loss_rate, version, updated_at)
VALUES (1, 1000000.00, 1000000.00, 0.00, 0.0000, 0, NOW())
ON CONFLICT (user_id) DO NOTHING;

-- 주식 종목
INSERT INTO stocks (code, name, market, is_active, created_at)
VALUES
    ('005930', '삼성전자', 'KOSPI', true, NOW()),
    ('035720', '카카오', 'KOSPI', true, NOW()),
    ('000660', 'SK하이닉스', 'KOSPI', true, NOW()),
    ('035420', 'NAVER', 'KOSPI', true, NOW()),
    ('051910', 'LG화학', 'KOSPI', true, NOW())
ON CONFLICT (code) DO NOTHING;