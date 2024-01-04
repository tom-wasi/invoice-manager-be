package com.tmszw.invoicemanagerv2.appuser;

import java.util.List;

public record AppUserDTO(
        Integer id,
        String username,
        String email,
        List<String> roles
){
}
