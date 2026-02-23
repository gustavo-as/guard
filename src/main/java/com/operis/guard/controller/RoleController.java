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

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findById(id));
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

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> update(@PathVariable Long id,
                                               @RequestBody UpdateRoleRequest request) {
        return ResponseEntity.ok(roleService.update(id, request));
    }

    @PostMapping("/{id}/permissions")
    public ResponseEntity<RoleResponse> addPermission(@PathVariable Long id,
                                                      @RequestBody RolePermissionRequest request) {
        return ResponseEntity.ok(roleService.addPermission(id, request));
    }

    @DeleteMapping("/{id}/permissions")
    public ResponseEntity<RoleResponse> removePermission(@PathVariable Long id,
                                                         @RequestBody RolePermissionRequest request) {
        return ResponseEntity.ok(roleService.removePermission(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}