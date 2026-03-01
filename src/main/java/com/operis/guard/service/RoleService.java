package com.operis.guard.service;

import com.operis.guard.dto.*;
import com.operis.guard.entity.Company;
import com.operis.guard.entity.Permission;
import com.operis.guard.entity.Role;
import com.operis.guard.entity.enummerator.RoleType;
import com.operis.guard.repository.CompanyRepository;
import com.operis.guard.repository.PermissionRepository;
import com.operis.guard.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final PermissionRepository permissionRepository;

    public RoleResponse create(CreateRoleRequest request) {
        if (roleRepository.existsByNameAndType(request.getName(), request.getType())) {
            throw new RuntimeException("Role already exists with this name and type");
        }

        Company company = null;
        if (request.getType() == RoleType.CUSTOM) {
            if (request.getCompanyId() == null) {
                throw new RuntimeException("Company is required for CUSTOM roles");
            }
            company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Company not found"));
        }

        Role role = Role.builder()
                .name(request.getName().toUpperCase())
                .type(request.getType())
                .company(company)
                .permissions(Set.of())
                .build();

        return toResponse(roleRepository.save(role));
    }

    public RoleResponse findByPublicId(String publicId) {
        return roleRepository.findByPublicId(publicId)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public List<RoleResponse> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<RoleResponse> findByType(RoleType type) {
        return roleRepository.findByType(type)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<RoleResponse> findByCompany(Long companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new RuntimeException("Company not found");
        }
        return roleRepository.findByCompanyId(companyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RoleResponse update(String publicId, UpdateRoleRequest request) {
        Role role = roleRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        role.setName(request.getName().toUpperCase());
        return toResponse(roleRepository.save(role));
    }

    public RoleResponse addPermission(String publicId, RolePermissionRequest request) {
        Role role = roleRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Permission permission = permissionRepository.findById(request.getPermissionId())
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        role.getPermissions().add(permission);
        return toResponse(roleRepository.save(role));
    }

    public RoleResponse removePermission(String publicId, RolePermissionRequest request) {
        Role role = roleRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Permission permission = permissionRepository.findById(request.getPermissionId())
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        role.getPermissions().remove(permission);
        return toResponse(roleRepository.save(role));
    }

    public void delete(Long publicId) {
        if (!roleRepository.existsById(publicId)) {
            throw new RuntimeException("Role not found");
        }
        roleRepository.deleteById(publicId);
    }

    private RoleResponse toResponse(Role role) {
        Set<PermissionResponse> permissions = role.getPermissions().stream()
                .map(p -> PermissionResponse.builder()
                        .publicId(p.getPublicId())
                        .name(p.getName())
                        .description(p.getDescription())
                        .build())
                .collect(Collectors.toSet());

        return RoleResponse.builder()
                .publicId(role.getPublicId())
                .name(role.getName())
                .type(role.getType())
                .companyPublicId(role.getCompany() != null ? role.getCompany().getPublicId() : null)
                .companyName(role.getCompany() != null ? role.getCompany().getName() : null)
                .permissions(permissions)
                .build();
    }
}