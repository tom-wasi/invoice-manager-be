package com.tmszw.invoicemanagerv2.auth;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

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
        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.token())
                .body(response);
    }

}
