package com.operis.guard.repository;

import com.operis.guard.entity.UserCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCompanyRepository extends JpaRepository<UserCompany, Long> {

    Optional<UserCompany> findByUserIdAndCompanyId(Long userId, Long companyId);

    Optional<UserCompany> findByUserIdAndCompanyPublicId(Long userId, String companyPublicId);

    List<UserCompany> findByUserIdAndActiveTrueOrderByCompanyNameAsc(Long userId);

}