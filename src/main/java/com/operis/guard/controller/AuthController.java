package com.operis.guard.controller;

import com.nimbusds.jwt.JWTClaimsSet;
import com.operis.guard.dto.*;
import com.operis.guard.service.AuthService;
import com.operis.guard.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/companies")
    public ResponseEntity<List<UserCompanyResponse>> getUserCompanies(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        JWTClaimsSet claims = jwtService.validateAndExtractClaims(token);
        String email = claims.getClaim("email").toString();

        return ResponseEntity.ok(authService.getUserCompanies(email));
    }

    @PostMapping("/switch-company")
    public ResponseEntity<AuthResponse> switchCompany(@RequestBody SwitchCompanyRequest request) {
        return ResponseEntity.ok(authService.switchCompany(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }
}