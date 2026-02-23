package com.operis.guard.dto;

import lombok.*;

@Getter
@Builder
public class PermissionResponse {

    private Long id;
    private String name;
    private String description;
}