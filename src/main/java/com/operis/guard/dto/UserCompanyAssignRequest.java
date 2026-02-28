package com.operis.guard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserCompanyAssignRequest {

    // PublicId da empresa a vincular
    private String companyPublicId;

    // PublicId da role a atribuir
    private String rolePublicId;
}