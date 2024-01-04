package com.tmszw.invoicemanagerv2.company;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/{userId}")
    public List<Company> getAllCompanies(@PathVariable("userId") Integer userId) {
        return companyService.getAllUserCompanies(userId);
    }

    @GetMapping("/company/{companyId}")
    public Company getCompanyById(@PathVariable("companyId") Integer companyId) {
        return companyService.getCompanyById(companyId);
    }

    @PostMapping("/{userId}/add-company")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addCompany(@PathVariable("userId") Integer userId, @RequestBody Company company) {
        companyService.addCompany(userId, company);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteCompany(@PathVariable("companyId") Integer companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateCompany(@PathVariable("companyId") Integer companyId, @RequestBody Company company) {
        companyService.updateCompany(companyId, company);
        return ResponseEntity.ok().build();
    }
}
