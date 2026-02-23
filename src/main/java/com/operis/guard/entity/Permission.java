package com.operis.guard.entity;

import jakarta.persistence.*;
import lombok.*;

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

    // Nome da permissão (ex: CREATE_USER, DELETE_USER, VIEW_REPORT)
    @Column(nullable = false, unique = true)
    private String name;

    // Descrição legível da permissão para exibição em interfaces
    private String description;
}