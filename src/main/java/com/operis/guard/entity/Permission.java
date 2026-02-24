package com.operis.guard.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "permission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identificador público exposto na API — nunca expor o id interno
    @Column(nullable = false, unique = true, updatable = false)
    private String publicId;

    // Nome da permissão (ex: CREATE_USER, DELETE_USER, VIEW_REPORT)
    @Column(nullable = false, unique = true)
    private String name;

    // Descrição legível da permissão para exibição em interfaces
    private String description;

    @PrePersist
    protected void onCreate() {
        publicId = UUID.randomUUID().toString();
    }

}