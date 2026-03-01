package com.operis.guard.controller;

import com.operis.guard.dto.*;
import com.operis.guard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<UserResponse> findById(@PathVariable String publicId) {
        return ResponseEntity.ok(userService.findByPublicId(publicId));
    }
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<UserResponse> update(@PathVariable String publicId,
                                               @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.update(publicId, request));
    }

    @PatchMapping("/{publicId}/status")
    public ResponseEntity<UserResponse> updateStatus(@PathVariable String publicId,
                                                     @RequestBody UpdateUserStatusRequest request) {
        return ResponseEntity.ok(userService.updateStatus(publicId, request));
    }

    @GetMapping("/{publicId}/companies")
    public ResponseEntity<List<UserCompanyResponse>> getUserCompanies(@PathVariable String publicId) {
        return ResponseEntity.ok(userService.getUserCompanies(publicId));
    }

    @PostMapping("/{publicId}/companies")
    public ResponseEntity<UserCompanyResponse> assignCompany(@PathVariable String publicId,
                                                             @RequestBody UserCompanyAssignRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.assignCompany(publicId, request));
    }

    @DeleteMapping("/{publicId}/companies/{companyPublicId}")
    public ResponseEntity<Void> removeCompany(@PathVariable String publicId,
                                              @PathVariable String companyPublicId) {
        userService.removeCompany(publicId, companyPublicId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{publicId}/hourly-rate")
    public ResponseEntity<UserCompanyResponse> updateHourlyRate(
            @PathVariable String publicId,
            @RequestBody UpdateHourlyRateRequest request) {
        return ResponseEntity.ok(userService.updateHourlyRate(
                publicId,
                request.getCompanyPublicId(),
                request.getHourlyRate()
        ));
    }

}