package com.operis.guard.controller;

import com.operis.guard.dto.CompanyResponse;
import com.operis.guard.dto.CreateCompanyRequest;
import com.operis.guard.dto.UpdateCompanyRequest;
import com.operis.guard.dto.UpdateUserStatusRequest;
import com.operis.guard.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyResponse> create(@RequestBody CreateCompanyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.findById(id));
    }

    @GetMapping("/registration/{registrationNumber}")
    public ResponseEntity<CompanyResponse> findByRegistrationNumber(@PathVariable String registrationNumber) {
        return ResponseEntity.ok(companyService.findByRegistrationNumber(registrationNumber));
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> findAll() {
        return ResponseEntity.ok(companyService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CompanyResponse>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(companyService.findByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> update(@PathVariable Long id,
                                                  @RequestBody UpdateCompanyRequest request) {
        return ResponseEntity.ok(companyService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CompanyResponse> updateStatus(@PathVariable Long id,
                                                        @RequestBody UpdateUserStatusRequest request) {
        return ResponseEntity.ok(companyService.updateStatus(id, request));
    }
}