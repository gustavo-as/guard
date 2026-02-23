package com.operis.guard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCompanyRequest {

    private String name;
    private String vatNumber;
    private String legalForm;
    private String purpose;
    private String street;
    private String streetNumber;
    private String postalCode;
    private String municipality;
    private String country;
}