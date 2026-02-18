package com.investment.exception;

/**
 * 비즈니스 로직에서 발생하는 예외
 * ex) 잔고 부족, 보유 주식 없음, 종목 없음 등의 상황
 *
 * IllegalArgumentException과의 차이는
 * - IllegalArgumentException: 잘못된 파라미터 (개발자 실수)
 * - BusinessException: 정상적인 요청이지만 비즈니스 규칙 위반 (사용자 상황)
 */
public class BusinessException extends RuntimeException{

    public BusinessException(String message) {
        super(message);
    }
}
