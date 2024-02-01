package com.tmszw.invoicemanagerv2.appuser;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("jpa")
public class AppUserJPADataAccessService implements AppUserDao {

    private final AppUserRepository appUserRepository;

    public AppUserJPADataAccessService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }
    @Override
    public Optional<AppUser> selectAppUserById(String id) {
        return appUserRepository.findById(id);
    }

    @Override
    public void insertAppUser(AppUser appUser) {
        appUserRepository.save(appUser);
    }

    @Override
    public boolean existsAppUserWithEmail(String email) {
        return appUserRepository.existsAppUserByEmail(email);
    }

    @Override
    public boolean existsAppUserById(String id) {
        return appUserRepository.existsAppUserById(id);
    }

    @Override
    public void deleteAppUserById(String appUserId) {
        appUserRepository.deleteById(appUserId);
    }

    @Override
    public void updateAppUser(AppUser update) {
        appUserRepository.save(update);
    }

    @Override
    public Optional<AppUser> selectAppUserByEmail(String email) {
        return Optional.ofNullable(appUserRepository.findAppUserByEmail(email));
    }

    @Override
    public AppUser findUserByEmailIgnoreCase(String email) {
        return appUserRepository.findAppUserByEmailIgnoreCase(email);
    }
}
