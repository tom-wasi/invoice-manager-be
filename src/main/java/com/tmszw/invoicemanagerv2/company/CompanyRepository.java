package com.tmszw.invoicemanagerv2.company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Query(value = "SELECT * FROM company WHERE user_id = ?1", nativeQuery = true)
    List<Company> findAllUserCompanies(String userId);

    @Query(value = "SELECT * FROM company WHERE company_id = ?1", nativeQuery = true)
    Company findCompanyById(Integer companyId);

}
