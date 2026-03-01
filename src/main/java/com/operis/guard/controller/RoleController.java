package com.operis.guard.controller;

import com.operis.guard.dto.*;
import com.operis.guard.entity.enummerator.RoleType;
import com.operis.guard.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleResponse> create(@RequestBody CreateRoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.create(request));
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<RoleResponse> findByPublicId(@PathVariable String publicId) {
        return ResponseEntity.ok(roleService.findByPublicId(publicId));
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> findAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<RoleResponse>> findByType(@PathVariable RoleType type) {
        return ResponseEntity.ok(roleService.findByType(type));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<RoleResponse>> findByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(roleService.findByCompany(companyId));
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<RoleResponse> update(@PathVariable String publicId,
                                               @RequestBody UpdateRoleRequest request) {
        return ResponseEntity.ok(roleService.update(publicId, request));
    }

    @PostMapping("/{publicId}/permissions")
    public ResponseEntity<RoleResponse> addPermission(@PathVariable String publicId,
                                                      @RequestBody RolePermissionRequest request) {
        return ResponseEntity.ok(roleService.addPermission(publicId, request));
    }

    @DeleteMapping("/{publicId}/permissions")
    public ResponseEntity<RoleResponse> removePermission(@PathVariable String publicId,
                                                         @RequestBody RolePermissionRequest request) {
        return ResponseEntity.ok(roleService.removePermission(publicId, request));
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<Void> delete(@PathVariable String publicId) {
        roleService.delete(publicId);
        return ResponseEntity.noContent().build();
    }
}