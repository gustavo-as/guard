package com.operis.guard.config;

import com.operis.guard.entity.*;
import com.operis.guard.entity.enumerator.RoleType;
import com.operis.guard.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Profile("!prod")
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserCompanyRepository userCompanyRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${app.seed.admin-email:admin@guard.io}")
    private String adminEmail;

    @Value("${app.seed.admin-password:Admin@Guard123}")
    private String adminPassword;

    private static final String ADMIN_ROLE_NAME = "ADMIN";
    private static final String SEED_COMPANY_REGISTRATION = "GUARD-SYSTEM-001";
    private static final String DEFAULT_PASSWORD = "Admin@Guard123";

    // Todas as permissões base do sistema — expanda conforme novos recursos forem adicionados
    private static final Map<String, String> PERMISSIONS = Map.ofEntries(
            Map.entry("CREATE_USER",        "Criar utilizadores"),
            Map.entry("READ_USER",          "Consultar utilizadores"),
            Map.entry("UPDATE_USER",        "Atualizar utilizadores"),
            Map.entry("DELETE_USER",        "Eliminar utilizadores"),
            Map.entry("CREATE_COMPANY",     "Criar empresas"),
            Map.entry("READ_COMPANY",       "Consultar empresas"),
            Map.entry("UPDATE_COMPANY",     "Atualizar empresas"),
            Map.entry("DELETE_COMPANY",     "Eliminar empresas"),
            Map.entry("CREATE_ROLE",        "Criar roles"),
            Map.entry("READ_ROLE",          "Consultar roles"),
            Map.entry("UPDATE_ROLE",        "Atualizar roles"),
            Map.entry("DELETE_ROLE",        "Eliminar roles"),
            Map.entry("CREATE_PERMISSION",  "Criar permissões"),
            Map.entry("READ_PERMISSION",    "Consultar permissões"),
            Map.entry("UPDATE_PERMISSION",  "Atualizar permissões"),
            Map.entry("DELETE_PERMISSION",  "Eliminar permissões"),
            Map.entry("ASSIGN_USER_COMPANY","Atribuir utilizadores a empresas")
    );

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        log.info("=== DataInitializer: iniciando seed de dados ===");

        Set<Permission> permissions = seedPermissions();
        Role adminRole = seedAdminRole(permissions);
        Company company = seedCompany();
        User adminUser = seedAdminUser();
        seedUserCompany(adminUser, company, adminRole);

        log.info("=== DataInitializer: seed concluído ===");
    }

    private Set<Permission> seedPermissions() {
        Set<Permission> result = new HashSet<>();
        for (Map.Entry<String, String> entry : PERMISSIONS.entrySet()) {
            Permission permission = permissionRepository.findByName(entry.getKey())
                    .orElseGet(() -> {
                        log.info("  [Permission] Criando: {}", entry.getKey());
                        return permissionRepository.save(
                                Permission.builder()
                                        .name(entry.getKey())
                                        .description(entry.getValue())
                                        .build()
                        );
                    });
            result.add(permission);
        }
        log.info("  [Permission] {} permissões garantidas", result.size());
        return result;
    }

    private Role seedAdminRole(Set<Permission> permissions) {
        return roleRepository.findByNameAndType(ADMIN_ROLE_NAME, RoleType.BASE)
                .map(existing -> {
                    log.info("  [Role] ADMIN já existe — garantindo permissões atualizadas");
                    existing.getPermissions().addAll(permissions);
                    return roleRepository.save(existing);
                })
                .orElseGet(() -> {
                    log.info("  [Role] Criando role ADMIN BASE");
                    return roleRepository.save(
                            Role.builder()
                                    .name(ADMIN_ROLE_NAME)
                                    .type(RoleType.BASE)
                                    .permissions(permissions)
                                    .build()
                    );
                });
    }

    private Company seedCompany() {
        return companyRepository.findByRegistrationNumber(SEED_COMPANY_REGISTRATION)
                .orElseGet(() -> {
                    log.info("  [Company] Criando empresa seed: Operis");
                    return companyRepository.save(
                            Company.builder()
                                    .name("Operis")
                                    .registrationNumber(SEED_COMPANY_REGISTRATION)
                                    .street("Rua do Sistema")
                                    .streetNumber("1")
                                    .postalCode("1000-001")
                                    .municipality("Lisboa")
                                    .country("PT")
                                    .active(true)
                                    .build()
                    );
                });
    }

    private User seedAdminUser() {
        return userRepository.findByEmail(adminEmail)
                .orElseGet(() -> {
                    if (DEFAULT_PASSWORD.equals(adminPassword)) {
                        log.warn("  [User] ATENÇÃO: usando password padrão para o admin! " +
                                "Defina 'app.seed.admin-password' nas variáveis de ambiente.");
                    }
                    log.info("  [User] Criando utilizador admin: {}", adminEmail);
                    return userRepository.save(
                            User.builder()
                                    .email(adminEmail)
                                    .password(passwordEncoder.encode(adminPassword))
                                    .active(true)
                                    .build()
                    );
                });
    }

    private void seedUserCompany(User user, Company company, Role role) {
        userCompanyRepository.findByUserIdAndCompanyId(user.getId(), company.getId())
                .orElseGet(() -> {
                    log.info("  [UserCompany] Vinculando admin à empresa: {}", company.getName());
                    return userCompanyRepository.save(
                            UserCompany.builder()
                                    .user(user)
                                    .company(company)
                                    .role(role)
                                    .active(true)
                                    .build()
                    );
                });
    }
}