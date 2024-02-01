package com.tmszw.invoicemanagerv2.appuser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface AppUserRepository extends JpaRepository<AppUser, String> {
    boolean existsAppUserByEmail(String email);
    boolean existsAppUserById(String id);
    AppUser findAppUserByEmail(String email);
    AppUser findAppUserByEmailIgnoreCase(String email);
}
