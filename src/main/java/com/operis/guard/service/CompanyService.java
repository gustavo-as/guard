package com.operis.guard.service;

import com.operis.guard.dto.CompanyResponse;
import com.operis.guard.dto.CreateCompanyRequest;
import com.operis.guard.dto.UpdateCompanyRequest;
import com.operis.guard.dto.UpdateUserStatusRequest;
import com.operis.guard.entity.Company;
import com.operis.guard.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyResponse create(CreateCompanyRequest request) {
        if (companyRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new RuntimeException("Registration number already in use");
        }

        Company company = Company.builder()
                .name(request.getName())
                .registrationNumber(request.getRegistrationNumber())
                .vatNumber(request.getVatNumber())
                .legalForm(request.getLegalForm())
                .purpose(request.getPurpose())
                .street(request.getStreet())
                .streetNumber(request.getStreetNumber())
                .postalCode(request.getPostalCode())
                .municipality(request.getMunicipality())
                .country(request.getCountry())
                .active(true)
                .build();

        return toResponse(companyRepository.save(company));
    }

    public CompanyResponse findById(Long id) {
        return companyRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    public CompanyResponse findByRegistrationNumber(String registrationNumber) {
        return companyRepository.findByRegistrationNumber(registrationNumber)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    public List<CompanyResponse> findAll() {
        return companyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CompanyResponse> findByName(String name) {
        return companyRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CompanyResponse update(Long id, UpdateCompanyRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setName(request.getName());
        company.setVatNumber(request.getVatNumber());
        company.setLegalForm(request.getLegalForm());
        company.setPurpose(request.getPurpose());
        company.setStreet(request.getStreet());
        company.setStreetNumber(request.getStreetNumber());
        company.setPostalCode(request.getPostalCode());
        company.setMunicipality(request.getMunicipality());
        company.setCountry(request.getCountry());

        return toResponse(companyRepository.save(company));
    }

    public CompanyResponse updateStatus(Long id, UpdateUserStatusRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setActive(request.getActive());
        return toResponse(companyRepository.save(company));
    }

    private CompanyResponse toResponse(Company company) {
        return CompanyResponse.builder()
                .publicId(company.getPublicId())
                .name(company.getName())
                .registrationNumber(company.getRegistrationNumber())
                .vatNumber(company.getVatNumber())
                .legalForm(company.getLegalForm())
                .purpose(company.getPurpose())
                .street(company.getStreet())
                .streetNumber(company.getStreetNumber())
                .postalCode(company.getPostalCode())
                .municipality(company.getMunicipality())
                .country(company.getCountry())
                .active(company.getActive())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }
}