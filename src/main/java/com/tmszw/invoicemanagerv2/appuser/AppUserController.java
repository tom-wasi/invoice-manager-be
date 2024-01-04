package com.tmszw.invoicemanagerv2.appuser;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AppUserController {

    private static final Logger logger = LoggerFactory.getLogger(AppUserController.class);

    private final AppUserService appUserService;

    @GetMapping("/{appUserId}")
    public AppUserDTO getAppUser(@PathVariable("appUserId") Integer appUserId) {
        return appUserService.getAppUser(appUserId);
    }

    @ApiOperation(value = "Register a new user", notes = "Registers a new user with provided details. User has to confirm registration by clicking the link in the email message.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User registered successfully"),
            @ApiResponse(code = 400, message = "Invalid registration request"),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerAppUser(@Valid @RequestBody AppUserRegistrationRequest request, BindingResult bindingResult) {

        logger.info("Attempting to register user with email: {}", request.email());

        appUserService.registerUser(request, bindingResult);

        if (bindingResult.hasErrors()) {
            logValidationErrors(bindingResult);
            return ResponseEntity.badRequest().body(validationErrors(bindingResult));
        }

        logger.info("User registration successful for email: {}", request.email());
        return ResponseEntity.ok("User registered successfully. Please confirm account with the link provided in the mail message");

    }

    @PutMapping("{appUserId}")
    public ResponseEntity<?> updateUser(@PathVariable("appUserId") Integer appUserId, AppUserUpdateRequest appUserUpdateRequest) {
        logger.info("Attempting to update user with id: {}", appUserId);
        appUserService.updateAppUser(appUserId, appUserUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{appUserId}")
    public ResponseEntity<?> deleteUser(@PathVariable("appUserId") Integer appUserId) {
        logger.info("Deleting user with id {}", appUserId);
        appUserService.deleteAppUser(appUserId);
        return ResponseEntity.ok().build();
    }

    private void logValidationErrors(BindingResult bindingResult) {
        bindingResult.getFieldErrors().forEach(error ->
                logger.warn("Validation error - Field: {}, Message: {}", error.getField(), error.getDefaultMessage()));
    }

    private Map<String, String> validationErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return errors;
    }
}
