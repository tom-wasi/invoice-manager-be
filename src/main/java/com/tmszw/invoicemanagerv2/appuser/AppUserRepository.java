package com.tmszw.invoicemanagerv2.appuser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    boolean existsAppUserByEmail(String email);
    boolean existsAppUserById(Integer id);
    Optional<AppUser> findAppUserByEmail(String email);
}
