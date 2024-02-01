package com.tmszw.invoicemanagerv2.company;

import com.tmszw.invoicemanagerv2.appuser.AppUser;

public record CompanyDTO(
    Integer id,
    String companyName,
    AppUser user,
    String accountantEmail
){
}
