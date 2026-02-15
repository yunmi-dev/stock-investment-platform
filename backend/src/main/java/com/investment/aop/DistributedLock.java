package com.investment.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 분산 락을 적용하기 위한 애노테이션
 * Redisson의 RLock을 사용하여 동시성 제어
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 락의 이름 (키 생성에 사용)
     */
    String key();

    /**
     * 락 획득을 시도하는 최대 시간 (기본: 5초)
     */
    long waitTime() default 5L;

    /**
     * 락을 자동으로 해제하는 시간 (기본: 3초)
     */
    long leaseTime() default 3L;

    /**
     * 시간 단위 (기본: 초)
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
