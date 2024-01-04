package com.tmszw.invoicemanagerv2.appuser;

public record AppUserUpdateRequest(
        String username,
        String email,
        String password
) {
}
