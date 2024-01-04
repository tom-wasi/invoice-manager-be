package com.tmszw.invoicemanagerv2.appuser;

import java.util.Optional;

public interface AppUserDao {
    Optional<AppUser> selectAppUserById(Integer appUserId);
    void insertAppUser(AppUser appUser);
    boolean existsAppUserWithEmail(String email);
    boolean existsAppUserById(Integer id);
    void deleteAppUserById(Integer appUserId);
    void updateAppUser(AppUser update);
    Optional<AppUser> selectAppUserByEmail(String email);
}
