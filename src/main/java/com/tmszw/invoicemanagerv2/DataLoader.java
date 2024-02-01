package com.tmszw.invoicemanagerv2;

import com.tmszw.invoicemanagerv2.appuser.AppUser;
import com.tmszw.invoicemanagerv2.appuser.AppUserRepository;
import com.tmszw.invoicemanagerv2.company.Company;
import com.tmszw.invoicemanagerv2.company.CompanyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final AppUserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(AppUserRepository appUserRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = appUserRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 1; i <= 10; i++) {
            String username = "user" + i;
            String email = "user" + i + "@example.com";
            String password = passwordEncoder.encode("password"); // You should use a strong password encoder

            String companyName = "company" + i;
            String accountantMail = "accountant" + i + "@gmail.com";

            AppUser newUser = AppUser.builder()
                    .username(username)
                    .email(email)
                    .password(password)
                    .isEnabled(true)
                    .build();

            userRepository.save(newUser);

            Company newCompany = Company.builder()
                    .companyName(companyName)
                    .accountantEmail(accountantMail)
                    .user(newUser)
                    .build();
            companyRepository.save(newCompany);
        }
    }
}