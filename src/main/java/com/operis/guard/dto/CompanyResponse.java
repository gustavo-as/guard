package com.operis.guard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.ALWAYS)
public class CompanyResponse {

    private String publicId;
    private String name;
    private String registrationNumber;
    private String vatNumber;
    private String legalForm;
    private String purpose;
    private String street;
    private String streetNumber;
    private String postalCode;
    private String municipality;
    private String country;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}