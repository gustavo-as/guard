package com.operis.guard.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_company")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identificador público exposto na API — nunca expor o id interno
    @Column(nullable = false, unique = true, updatable = false)
    private String publicId;

    // Usuário vinculado à empresa
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Empresa à qual o usuário está vinculado
    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // Papel do usuário dentro desta empresa
    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // Hora de trabalho do usuario na empresa vinculada
    @Column(precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    // Indica se este vínculo está ativo
    @Column(nullable = false)
    private Boolean active;

    // Data de criação do vínculo — preenchida automaticamente
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        publicId = UUID.randomUUID().toString();
        createdAt = LocalDateTime.now();
    }
}