package com.tmszw.invoicemanagerv2.auth;

import com.tmszw.invoicemanagerv2.appuser.AppUserDTO;

public record AuthenticationResponse(
        String token,
        AppUserDTO appUserDTO
) {
}
