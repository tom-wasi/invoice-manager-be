package com.tmszw.invoicemanagerv2.appuser;

import java.util.Optional;

public interface AppUserDao {
    Optional<AppUser> selectAppUserById(String userId);
    void insertAppUser(AppUser appUser);
    boolean existsAppUserWithEmail(String email);
    boolean existsAppUserById(String id);
    void deleteAppUserById(String appUserId);
    void updateAppUser(AppUser update);
    Optional<AppUser> selectAppUserByEmail(String email);
    AppUser findUserByEmailIgnoreCase(String email);
}
