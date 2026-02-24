package com.operis.guard.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Token gerado aleatoriamente e armazenado em base de dados
    @Column(nullable = false, unique = true)
    private String token;

    // Utilizador ao qual o token pertence
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Empresa ativa no momento da autenticação
    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // Data de expiração do refresh token
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    // Indica se o token já foi utilizado ou revogado
    @Column(nullable = false)
    private Boolean revoked;

    // Data de criação do token
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}