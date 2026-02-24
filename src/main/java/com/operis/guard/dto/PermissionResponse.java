package com.operis.guard.dto;

import lombok.*;

@Getter
@Builder
public class PermissionResponse {

    private String publicId;
    private String name;
    private String description;
}