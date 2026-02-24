package com.operis.guard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.operis.guard.entity.enummerator.RoleType;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.ALWAYS)
public class RoleResponse {

    private Long id;
    private String name;
    private RoleType type;

    // Presente apenas quando type = CUSTOM
    private Long companyId;
    private String companyName;

    private Set<PermissionResponse> permissions;
}