package com.tmszw.invoicemanagerv2.auth;

import com.tmszw.invoicemanagerv2.appuser.AppUser;
import com.tmszw.invoicemanagerv2.appuser.AppUserDTO;
import com.tmszw.invoicemanagerv2.appuser.AppUserDTOMapper;
import com.tmszw.invoicemanagerv2.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final AppUserDTOMapper appUserDTOMapper;
    private final JWTUtil jwtUtil;

    public AuthenticationResponse login(AuthenticationRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        AppUser principal = (AppUser) authentication.getPrincipal();
        AppUserDTO appUserDTO = appUserDTOMapper.apply(principal);
        String token = jwtUtil.issueToken(appUserDTO.email(), appUserDTO.roles());
        return new AuthenticationResponse(token, appUserDTO);
    }

}
