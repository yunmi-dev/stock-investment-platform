package com.investment.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing 설정
 * 여기서 @CreatedDate, @LastModifiedDate 자동 처리
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}