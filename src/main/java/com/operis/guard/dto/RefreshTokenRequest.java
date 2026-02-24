package com.operis.guard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class RefreshTokenRequest {

    private String refreshToken;
}