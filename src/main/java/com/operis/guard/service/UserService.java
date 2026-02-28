package com.operis.guard.service;

import com.operis.guard.dto.*;
import com.operis.guard.entity.Company;
import com.operis.guard.entity.Role;
import com.operis.guard.entity.User;
import com.operis.guard.entity.UserCompany;
import com.operis.guard.repository.CompanyRepository;
import com.operis.guard.repository.RoleRepository;
import com.operis.guard.repository.UserCompanyRepository;
import com.operis.guard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserCompanyRepository userCompanyRepository;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;


    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .build();

        return toResponse(userRepository.save(user));
    }

    public UserResponse findByPublicId(String publicId) {
        return userRepository.findByPublicId(publicId)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse updateStatus(String publicId, UpdateUserStatusRequest request) {
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(request.getActive());
        return toResponse(userRepository.save(user));
    }

    public UserResponse update(String publicId, CreateUserRequest request) {
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return toResponse(userRepository.save(user));
    }

    public List<UserCompanyResponse> getUserCompanies(String publicId) {
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userCompanyRepository
                .findByUserIdAndActiveTrueOrderByCompanyNameAsc(user.getId())
                .stream()
                .map(uc -> UserCompanyResponse.builder()
                        .companyPublicId(uc.getCompany().getPublicId())
                        .companyName(uc.getCompany().getName())
                        .role(uc.getRole().getName())
                        .build())
                .toList();
    }

    public UserCompanyResponse assignCompany(String publicId, UserCompanyAssignRequest request) {
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Company company = companyRepository.findByPublicId(request.getCompanyPublicId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Role role = roleRepository.findByPublicId(request.getRolePublicId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (userCompanyRepository.findByUserIdAndCompanyId(user.getId(), company.getId()).isPresent()) {
            throw new RuntimeException("User already linked to this company");
        }

        UserCompany userCompany = UserCompany.builder()
                .user(user)
                .company(company)
                .role(role)
                .active(true)
                .build();

        userCompanyRepository.save(userCompany);

        return UserCompanyResponse.builder()
                .companyPublicId(company.getPublicId())
                .companyName(company.getName())
                .role(role.getName())
                .build();
    }

    public void removeCompany(String userPublicId, String companyPublicId) {
        User user = userRepository.findByPublicId(userPublicId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Company company = companyRepository.findByPublicId(companyPublicId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        UserCompany userCompany = userCompanyRepository
                .findByUserIdAndCompanyId(user.getId(), company.getId())
                .orElseThrow(() -> new RuntimeException("User is not linked to this company"));

        userCompanyRepository.delete(userCompany);
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .publicId(user.getPublicId())
                .email(user.getEmail())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}