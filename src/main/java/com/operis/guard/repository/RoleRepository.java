package com.operis.guard.repository;

import com.operis.guard.entity.Role;
import com.operis.guard.entity.enummerator.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByType(RoleType type);

    List<Role> findByCompanyId(Long companyId);

    boolean existsByNameAndType(String name, RoleType type);

    Optional<Role> findByPublicId(String publicId);

}