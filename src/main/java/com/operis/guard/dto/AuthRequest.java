package com.operis.guard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class AuthRequest {

    private String email;
    private String password;

}