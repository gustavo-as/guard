package com.operis.guard.dto;

import com.operis.guard.entity.enummerator.RoleType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CreateRoleRequest {

    private String name;
    private RoleType type;

    // Obrigatório apenas quando type = CUSTOM
    private Long companyId;
}