package com.operis.guard.service;

import com.operis.guard.dto.AuthRequest;
import com.operis.guard.dto.AuthResponse;
import com.operis.guard.dto.RefreshTokenRequest;
import com.operis.guard.dto.SwitchCompanyRequest;
import com.operis.guard.entity.RefreshToken;
import com.operis.guard.entity.User;
import com.operis.guard.entity.UserCompany;
import com.operis.guard.repository.RefreshTokenRepository;
import com.operis.guard.repository.UserCompanyRepository;
import com.operis.guard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserCompanyRepository userCompanyRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.getActive()) {
            throw new RuntimeException("User account is disabled");
        }

        // Busca empresas do utilizador ordenadas alfabeticamente
        List<UserCompany> userCompanies = userCompanyRepository
                .findByUserIdAndActiveTrueOrderByCompanyNameAsc(user.getId());

        if (userCompanies.isEmpty()) {
            throw new RuntimeException("User has no active company access");
        }

        // Seleciona automaticamente a primeira empresa (ordem alfabética)
        UserCompany userCompany = userCompanies.get(0);

        List<String> permissions = userCompany.getRole().getPermissions()
                .stream()
                .map(p -> p.getName())
                .toList();

        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getEmail(),
                userCompany.getCompany().getPublicId(),
                userCompany.getRole().getName(),
                permissions
        );

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .company(userCompany.getCompany())
                .expiresAt(LocalDateTime.now().plusSeconds(604800))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .email(user.getEmail())
                .companyPublicId(userCompany.getCompany().getPublicId())
                .companyName(userCompany.getCompany().getName())
                .role(userCompany.getRole().getName())
                .permissions(permissions)
                .build();
    }

    public AuthResponse switchCompany(SwitchCompanyRequest request) {
        // Valida o refresh token
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getRevoked()) {
            throw new RuntimeException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token has expired");
        }

        User user = refreshToken.getUser();

        // Busca o vínculo com a nova empresa
        UserCompany userCompany = userCompanyRepository
                .findByUserIdAndCompanyPublicId(user.getId(), request.getCompanyPublicId())
                .orElseThrow(() -> new RuntimeException("User has no access to this company"));

        if (!userCompany.getActive()) {
            throw new RuntimeException("User access to this company is disabled");
        }

        List<String> permissions = userCompany.getRole().getPermissions()
                .stream()
                .map(p -> p.getName())
                .toList();

        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getEmail(),
                userCompany.getCompany().getPublicId(),
                userCompany.getRole().getName(),
                permissions
        );

        // Revoga o refresh token atual e gera um novo
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        RefreshToken newRefreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .company(userCompany.getCompany())
                .expiresAt(LocalDateTime.now().plusSeconds(604800))
                .revoked(false)
                .build();

        refreshTokenRepository.save(newRefreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken.getToken())
                .email(user.getEmail())
                .companyPublicId(userCompany.getCompany().getPublicId())
                .companyName(userCompany.getCompany().getName())
                .role(userCompany.getRole().getName())
                .permissions(permissions)
                .build();
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getRevoked()) {
            throw new RuntimeException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token has expired");
        }

        User user = refreshToken.getUser();

        UserCompany userCompany = userCompanyRepository
                .findByUserIdAndCompanyId(user.getId(), refreshToken.getCompany().getId())
                .orElseThrow(() -> new RuntimeException("User company access not found"));

        List<String> permissions = userCompany.getRole().getPermissions()
                .stream()
                .map(p -> p.getName())
                .toList();

        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getEmail(),
                userCompany.getCompany().getPublicId(),
                userCompany.getRole().getName(),
                permissions
        );

        // Revoga o refresh token atual e gera um novo
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        RefreshToken newRefreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .company(userCompany.getCompany())
                .expiresAt(LocalDateTime.now().plusSeconds(604800))
                .revoked(false)
                .build();

        refreshTokenRepository.save(newRefreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken.getToken())
                .email(user.getEmail())
                .companyPublicId(userCompany.getCompany().getPublicId())
                .companyName(userCompany.getCompany().getName())
                .role(userCompany.getRole().getName())
                .permissions(permissions)
                .build();
    }

    public void logout(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }
}