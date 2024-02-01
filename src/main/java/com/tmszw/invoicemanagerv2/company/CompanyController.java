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

    @GetMapping("/get-companies")
    public List<CompanyDTO> getAllCompanies(@RequestParam String userId) {
        return companyService.getAllUserCompanies(userId);
    }

    @GetMapping("/get-company")
    public CompanyDTO getCompanyById(@RequestParam Integer companyId) {
        return companyService.getCompanyDTOById(companyId);
    }

    @PostMapping("/add-company")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addCompany(@RequestBody CompanyRequest company, @RequestParam String userId) {
        companyService.addCompany(userId, company);
        return ResponseEntity.ok().body("Company created successfully!");
    }

    @DeleteMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteCompany(@PathVariable("companyId") Integer companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateCompany(@PathVariable("companyId") Integer companyId, @RequestBody CompanyUpdateRequest company) {
        companyService.updateCompany(companyId, company);
        return ResponseEntity.ok().build();
    }
}
