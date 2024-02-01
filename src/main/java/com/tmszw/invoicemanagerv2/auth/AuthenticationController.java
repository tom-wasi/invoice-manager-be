package com.tmszw.invoicemanagerv2.auth;

import com.tmszw.invoicemanagerv2.appuser.AppUser;
import com.tmszw.invoicemanagerv2.appuser.AppUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AppUserService appUserService;

    @ApiOperation(
            value = "Login endpoint for the user",
            notes = "Authenticates a user with provided credentials. " +
                    "Responds with a JWT token if authentication is successful.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200, message = "User authenticated successfully"
                    ),
                    @ApiResponse(
                            code = 400, message = "Invalid authentication credentials"
                    ),
            })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        Optional<AppUser> appUser = appUserService.getUserByEmail(request.email());

        if(appUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

            AuthenticationResponse response = authenticationService.login(request);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, response.token())
                    .body(response);
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token") String confirmationToken) {
        return appUserService.confirmEmail(confirmationToken);
    }

}
