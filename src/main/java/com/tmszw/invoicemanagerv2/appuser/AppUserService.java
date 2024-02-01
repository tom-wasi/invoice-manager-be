package com.tmszw.invoicemanagerv2.appuser;

import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.tmszw.invoicemanagerv2.mail.MailService;
import com.tmszw.invoicemanagerv2.mail.confirmation.ConfirmationToken;
import com.tmszw.invoicemanagerv2.mail.confirmation.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserService {

    private final AppUserDao appUserDao;
    private final AppUserDTOMapper appUserDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final MailService mailService;

    @Value("${app.base-url}")
    private String url;

    public AppUserService(@Qualifier("jpa")
                          AppUserDao appUserDao,
                          AppUserDTOMapper appUserDTOMapper,
                          PasswordEncoder passwordEncoder, ConfirmationTokenRepository confirmationTokenRepository, MailService mailService) {
        this.appUserDao = appUserDao;
        this.appUserDTOMapper = appUserDTOMapper;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.mailService = mailService;
    }

    public void registerUser(AppUserRegistrationRequest request, BindingResult bindingResult) {

        validateRegistrationRequest(request, bindingResult);

        if (!bindingResult.hasErrors()) {
            AppUser user = buildUser(request);

            appUserDao.insertAppUser(user);
            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            confirmationTokenRepository.save(confirmationToken);
            sendConfirmationEmail(user, confirmationToken);
        }
    }

    public AppUserDTO getAppUser(String appUserId) {
        return appUserDao.selectAppUserById(appUserId)
                .map(appUserDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("User with id [%s] not found".formatted(appUserId)));
    }

    public AppUser getUser(String appUserId) {
        return appUserDao.selectAppUserById(appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id [%s] not found".formatted(appUserId)));
    }

    public void updateAppUser(String appUserId, AppUserUpdateRequest updateRequest) {
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
                .id(UUID.randomUUID().toString())
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .isEnabled(false)
                .build();
    }

    public void deleteAppUser(String appUserId) {
        appUserDao.deleteAppUserById(appUserId);
    }

    private void sendConfirmationEmail(AppUser user, ConfirmationToken confirmationToken) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete the registration!");
        mailMessage.setText("To confirm your account, please click here: "
                + url + "/api/v1/auth/confirm-account?token=" + confirmationToken.getConfirmationToken());
        mailService.sendEmail(mailMessage);
    }

    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token != null) {
            AppUser user = appUserDao.findUserByEmailIgnoreCase(token.getAppUser().getEmail());
            user.setEnabled(true);
            appUserDao.insertAppUser(user);
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");

    }

    public Optional<AppUser> getUserByEmail(String email) {
        return appUserDao.selectAppUserByEmail(email);
    }

    public boolean getIsEnabled(String email) {
        AppUser appUser = appUserDao.selectAppUserByEmail(email).orElseThrow();
        return appUser.isEnabled();
    }
}