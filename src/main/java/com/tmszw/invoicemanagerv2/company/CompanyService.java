package com.tmszw.invoicemanagerv2.company;

import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.tmszw.invoicemanagerv2.appuser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.tmszw.invoicemanagerv2.appuser.AppUser;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final AppUserService appUserService;

    private final CompanyRepository companyRepository;

    public List<Company> getAllUserCompanies(Integer userId) {
        return companyRepository.findAllUserCompanies(userId);
    }

    public Company getCompanyById(Integer companyId) {
        return companyRepository.findCompanyById(companyId);
    }

    public void addCompany(Integer userId, Company company) {

        AppUser appUser =  appUserService.getUser(userId);
        if(appUser == null) {
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));
        }

        if(company.getCompanyName() == null || company.getCompanyName().isEmpty()) {
            throw new IllegalArgumentException("Request body cannot be null nor blank");
        }

        Company newCompany = new Company();
        newCompany.setCompanyName(company.getCompanyName());
        newCompany.setUserId(appUser);
        companyRepository.save(newCompany);
    }

    public void deleteCompany(Integer companyId) {
        companyRepository.deleteById(companyId);
    }

    public void updateCompany(Integer companyId, Company company) {
        Company existingCompany = companyRepository.findCompanyById(companyId);

        if(existingCompany == null) {
            throw new ResourceNotFoundException("company with id [%s] not found".formatted(companyId));
        }

        existingCompany.setCompanyName(company.getCompanyName());
        companyRepository.save(existingCompany);
    }

    public boolean existsById(Integer companyId) {
        return companyRepository.existsById(companyId);
    }
}