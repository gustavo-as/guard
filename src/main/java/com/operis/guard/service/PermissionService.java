package com.operis.guard.service;

import com.operis.guard.dto.CreatePermissionRequest;
import com.operis.guard.dto.PermissionResponse;
import com.operis.guard.entity.Permission;
import com.operis.guard.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionResponse create(CreatePermissionRequest request) {
        if (permissionRepository.existsByName(request.getName())) {
            throw new RuntimeException("Permission already exists");
        }

        Permission permission = Permission.builder()
                .name(request.getName().toUpperCase())
                .description(request.getDescription())
                .build();

        return toResponse(permissionRepository.save(permission));
    }

    public PermissionResponse findById(Long id) {
        return permissionRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
    }

    public List<PermissionResponse> findAll() {
        return permissionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PermissionResponse update(Long id, CreatePermissionRequest request) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        permission.setName(request.getName().toUpperCase());
        permission.setDescription(request.getDescription());

        return toResponse(permissionRepository.save(permission));
    }

    public void delete(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new RuntimeException("Permission not found");
        }
        permissionRepository.deleteById(id);
    }

    private PermissionResponse toResponse(Permission permission) {
        return PermissionResponse.builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getDescription())
                .build();
    }
}