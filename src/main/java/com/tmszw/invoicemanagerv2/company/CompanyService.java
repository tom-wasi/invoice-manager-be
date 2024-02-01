package com.tmszw.invoicemanagerv2.company;

import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.tmszw.invoicemanagerv2.appuser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.tmszw.invoicemanagerv2.appuser.AppUser;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final AppUserService appUserService;
    private final CompanyRepository companyRepository;
    private final CompanyDTOMapper companyDTOMapper;

    public List<CompanyDTO> getAllUserCompanies(String userId) {
        List<Company> companies = companyRepository.findAllUserCompanies(userId);
        List<CompanyDTO> companyDTOS = new ArrayList<>();
        for (Company c : companies) {
            companyDTOS.add(companyDTOMapper.apply(c));
        }
        return companyDTOS;
    }

    public CompanyDTO getCompanyDTOById(Integer companyId) {
        return companyDTOMapper.apply(companyRepository.findCompanyById(companyId));
    }

    public Company getCompanyById(Integer companyId) {
        return companyRepository.findCompanyById(companyId);
    }

    public void addCompany(String userId, CompanyRequest company) {

        AppUser appUser = appUserService.getUser(userId);
        if (appUser == null) {
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));
        }

        if (company.companyName() == null || company.companyName().isEmpty()) {
            throw new IllegalArgumentException("Request body cannot be null nor blank");
        }

        Company newCompany = new Company();
        newCompany.setCompanyName(company.companyName());
        newCompany.setAccountantEmail(company.accountantEmail());
        newCompany.setUser(appUser);
        companyRepository.save(newCompany);
    }

    public void deleteCompany(Integer companyId) {
        companyRepository.deleteById(companyId);
    }

    public void updateCompany(Integer companyId, CompanyUpdateRequest companyUpdateRequest) {
        Company existingCompany = companyRepository.findCompanyById(companyId);

        if (existingCompany == null) {
            throw new ResourceNotFoundException("company with id [%s] not found".formatted(companyId));
        }

        existingCompany.setCompanyName(companyUpdateRequest.companyName());
        existingCompany.setAccountantEmail(companyUpdateRequest.accountantEmail());
        companyRepository.save(existingCompany);
    }

    public boolean existsById(Integer companyId) {
        return companyRepository.existsById(companyId);
    }
}