package com.operis.guard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CreatePermissionRequest {

    private String name;
    private String description;
}