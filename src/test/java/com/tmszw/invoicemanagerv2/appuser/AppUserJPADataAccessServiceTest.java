package com.tmszw.invoicemanagerv2.appuser;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.verify;

class AppUserJPADataAccessServiceTest {

    private AppUserJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private AppUserRepository appUserRepository;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AppUserJPADataAccessService(appUserRepository);
    }
    @AfterEach
    void teardown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAppUser__ById() {
        //given
        String id = UUID.randomUUID().toString();

        //when
        underTest.selectAppUserById(id);

        //then
        verify(appUserRepository).findById(id);
    }

    @Test
    void insertAppUser() {
        //given
        AppUser appUser = new AppUser(
                UUID.randomUUID().toString(), "username", "username@emaple.com", "12341234", true
        );

        //when
        underTest.insertAppUser(appUser);

        //then
        verify(appUserRepository).save(appUser);
    }

    @Test
    void existsAppUser__WithEmail() {
        //given
        String email = "username@example.com";

        //when
        underTest.existsAppUserWithEmail(email);

        //then
        verify(appUserRepository).existsAppUserByEmail(email);
    }

    @Test
    void existsAppUser__ById() {
        //given
        String id = UUID.randomUUID().toString();

        //when
        underTest.existsAppUserById(id);

        //then
        verify(appUserRepository).existsAppUserById(id);
    }

    @Test
    void deleteAppUser__ById() {
        //given
        String id = UUID.randomUUID().toString();

        //when
        underTest.deleteAppUserById(id);

        //then
        verify(appUserRepository).deleteById(id);
    }

    @Test
    void updateAppUser() {
        //given
        AppUser appUser = new AppUser(
                UUID.randomUUID().toString(), "username", "username@emaple.com", "12341234", true
        );

        //when
        underTest.updateAppUser(appUser);

        //then
        verify(appUserRepository).save(appUser);
    }

}
