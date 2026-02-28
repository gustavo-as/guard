package com.operis.guard.dto;

import lombok.*;

@Getter
@Builder
public class UserCompanyResponse {

    private String companyPublicId;
    private String companyName;
    private String role;
}