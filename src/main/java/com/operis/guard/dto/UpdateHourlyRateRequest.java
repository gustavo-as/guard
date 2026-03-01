package com.operis.guard.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class UpdateHourlyRateRequest {
    private String companyPublicId;
    private BigDecimal hourlyRate;
}