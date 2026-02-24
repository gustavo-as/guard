package com.operis.guard.entity;

import com.operis.guard.entity.enummerator.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identificador público exposto na API — nunca expor o id interno
    @Column(nullable = false, unique = true, updatable = false)
    private String publicId;

    // Nome do papel (ex: ADMIN, MANAGER, USER)
    @Column(nullable = false)
    private String name;

    // Tipo do papel — BASE para roles globais, CUSTOM para roles criadas por empresa
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType type;

    // Empresa proprietária da role — nulo se for uma role BASE
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    // Permissões associadas a este papel
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

    @PrePersist
    protected void onCreate() {
        publicId = UUID.randomUUID().toString();
    }

}