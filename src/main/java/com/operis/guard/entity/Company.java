package com.operis.guard.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "company")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome oficial da empresa conforme registo comercial
    @Column(nullable = false)
    private String name;

    // Número de registo nacional da empresa (ex: KBO na Bélgica, NIPC em Portugal)
    @Column(nullable = false, unique = true)
    private String registrationNumber;

    // Número de IVA europeu — inclui prefixo do país (ex: BE0123456789, PT123456789)
    @Column(unique = true)
    private String vatNumber;

    // Forma jurídica da empresa (ex: BV/SRL na Bélgica, Lda/SA em Portugal)
    private String legalForm;

    // Objeto social — descrição da atividade principal da empresa
    private String purpose;

    // Nome da rua da sede social
    @Column(nullable = false)
    private String street;

    // Número da porta da sede social
    private String streetNumber;

    // Código postal da sede social
    @Column(nullable = false)
    private String postalCode;

    // Município/cidade da sede social
    @Column(nullable = false)
    private String municipality;

    // Código ISO do país (ex: BE para Bélgica, PT para Portugal, DE para Alemanha)
    @Column(nullable = false, length = 2)
    private String country;

    // Indica se a empresa está ativa no sistema
    @Column(nullable = false)
    private Boolean active;

    // Data de criação do registo — preenchida automaticamente
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Data da última atualização do registo — atualizada automaticamente
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}