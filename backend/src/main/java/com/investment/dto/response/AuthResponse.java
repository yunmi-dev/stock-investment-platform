package com.investment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private String token;   // 클라이언트가 이후 요청마다 헤더에 담을 JWT
    private String email;
    private String name;
}
