package com.operis.guard.service;

import com.operis.guard.config.RsaKeyConfig;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final RsaKeyConfig rsaKeyConfig;

    public String generateAccessToken(Long userId, String email, String companyPublicId, String role, List<String> permissions) {
        try {
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(String.valueOf(userId))
                    .issueTime(Date.from(Instant.now()))
                    .expirationTime(Date.from(Instant.now().plusSeconds(rsaKeyConfig.accessTokenExpiration())))
                    .claim("email", email)
                    .claim("companyPublicId", companyPublicId)
                    .claim("role", role)
                    .claim("permissions", permissions)
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.RS256),
                    claims
            );

            signedJWT.sign(new RSASSASigner(rsaKeyConfig.privateKey()));
            return signedJWT.serialize();

        } catch (Exception e) {
            throw new RuntimeException("Error generating access token", e);
        }
    }

    public JWTClaimsSet validateAndExtractClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new RSASSAVerifier(rsaKeyConfig.publicKey());

            if (!signedJWT.verify(verifier)) {
                throw new RuntimeException("Invalid JWT signature");
            }

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            if (claims.getExpirationTime().before(Date.from(Instant.now()))) {
                throw new RuntimeException("JWT token expired");
            }

            return claims;

        } catch (Exception e) {
            throw new RuntimeException("Error validating token", e);
        }
    }

    public boolean isTokenValid(String token) {
        try {
            validateAndExtractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}