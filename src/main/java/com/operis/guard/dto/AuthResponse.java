package com.operis.guard.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String email;
    private Long companyId;
    private String companyName;
    private String role;
    private List<String> permissions;
}