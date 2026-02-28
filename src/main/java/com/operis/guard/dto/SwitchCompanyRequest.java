package com.operis.guard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class SwitchCompanyRequest {

    private String companyPublicId;
    private String refreshToken;
}