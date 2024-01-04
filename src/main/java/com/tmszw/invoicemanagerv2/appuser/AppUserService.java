package com.tmszw.invoicemanagerv2.appuser;

import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class AppUserService {

    private final AppUserDao appUserDao;
    private final AppUserDTOMapper appUserDTOMapper;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(@Qualifier("jpa")
                          AppUserDao appUserDao,
                          AppUserDTOMapper appUserDTOMapper,
                          PasswordEncoder passwordEncoder) {
        this.appUserDao = appUserDao;
        this.appUserDTOMapper = appUserDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(AppUserRegistrationRequest request, BindingResult bindingResult) {

        validateRegistrationRequest(request, bindingResult);

        if(!bindingResult.hasErrors()) {
            AppUser user = buildUser(request);
            appUserDao.insertAppUser(user);
        }
    }

    public AppUserDTO getAppUser(Integer appUserId) {
        return appUserDao.selectAppUserById(appUserId)
                .map(appUserDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("User with id [%s] not found".formatted(appUserId)));
    }

    public AppUser getUser(Integer appUserId) {
        return appUserDao.selectAppUserById(appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id [%s] not found".formatted(appUserId)));
    }

    public void updateAppUser(Integer appUserId, AppUserUpdateRequest updateRequest) {
        AppUser appUser = appUserDao.selectAppUserById(appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id [%s] not found".formatted(appUserId)));

        boolean changesFlag = false;

        if (updateRequest.username() != null && !updateRequest.username().equals(appUser.getUsername())) {
            appUser.setUsername(updateRequest.username());
            changesFlag = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(appUser.getEmail())) {
            appUser.setEmail(updateRequest.email());
            changesFlag = true;
        }

        if (updateRequest.password() != null && !passwordEncoder.matches(updateRequest.password(), appUser.getPassword())) {
            appUser.setPassword(updateRequest.password());
            changesFlag = true;
        }

        if (!changesFlag) {
            throw new IllegalArgumentException("No changes to update");
        }
        appUserDao.updateAppUser(appUser);
    }

    public boolean emailExists(String email) {
        return appUserDao.existsAppUserWithEmail(email);
    }

    public void validateRegistrationRequest(AppUserRegistrationRequest request, BindingResult bindingResult) {

        if (emailExists(request.email())) {
            bindingResult.rejectValue("email", "email.exists", "Email already registered");
        }

        if (request.password() != null && request.password().length() < 8) {
            bindingResult.rejectValue("password", "password.short", "Password must be at least 8 characters long");
        }
    }
    private AppUser buildUser(AppUserRegistrationRequest request) {
        return AppUser.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
    }

    public void deleteAppUser(Integer appUserId) {
        appUserDao.deleteAppUserById(appUserId);
    }
}