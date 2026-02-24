package com.operis.guard.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "jwt")
public record RsaKeyConfig(
        RSAPublicKey publicKey,
        RSAPrivateKey privateKey,
        Long accessTokenExpiration,
        Long refreshTokenExpiration
) {}