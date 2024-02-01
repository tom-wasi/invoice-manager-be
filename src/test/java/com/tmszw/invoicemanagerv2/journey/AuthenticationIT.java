package com.tmszw.invoicemanagerv2.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.tmszw.invoicemanagerv2.appuser.*;
import com.tmszw.invoicemanagerv2.auth.AuthenticationRequest;
import com.tmszw.invoicemanagerv2.auth.AuthenticationResponse;
import com.tmszw.invoicemanagerv2.jwt.JWTUtil;
import com.tmszw.invoicemanagerv2.mail.confirmation.ConfirmationToken;
import com.tmszw.invoicemanagerv2.mail.confirmation.ConfirmationTokenRepository;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private JWTUtil jwtUtil;

    private static final String AUTHENTICATION_PATH = "/api/v1/auth";

    private static final String USER_PATH = "/api/v1/users";

    @Test
    void canLogin() {
        //given
        //create appUserRegistrationRequest
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.lastName() + "_" + UUID.randomUUID() + "@tmszw.com";

        String password = "password";

        AppUserRegistrationRequest appUserRegistrationRequest = new AppUserRegistrationRequest(
                name, email, password
        );

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                email,
                password
        );

        //send a post with authenticationRequest to check if a user can't log in unless account is created and confirmed
        webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.NOT_FOUND);

        //send a post with appUserRegistrationRequest
        webTestClient.post()
                .uri(USER_PATH + "/register-user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Mono.just(appUserRegistrationRequest),
                        AppUserRegistrationRequest.class
                )
                .exchange()
                .expectStatus()
                .isOk();

        //check again to see if still cannot log in when not verified
        webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();

        AppUser user = appUserRepository.findAppUserByEmail(email);
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByAppUserId(user.getId());
        String token = confirmationToken.getConfirmationToken();

        //send a post with confirmationToken
        webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/confirm-account?token=" + token)
                .exchange()
                .expectStatus()
                .isOk();

        //send an authenticationRequest to correctly log in
        EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                })
                .returnResult();

        String jwtToken = Objects.requireNonNull(Objects.requireNonNull(result.getResponseHeaders()
                        .get(HttpHeaders.AUTHORIZATION)))
                .get(0);

        String decodedIdFromToken = jwtUtil.getClaims(jwtToken).getSubject();

        EntityExchangeResult<AppUserDTO> fetchedUser = webTestClient.get()
                .uri(USER_PATH + "/" + decodedIdFromToken)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AppUserDTO>() {
                })
                .returnResult();

        AppUserDTO appUserDTO = fetchedUser.getResponseBody();

        assertThat(jwtUtil.isTokenValid(
                jwtToken,
                Objects.requireNonNull(appUserDTO).id())).isTrue();

        assertThat(appUserDTO.email()).isEqualTo(email);
        assertThat(appUserDTO.username()).isEqualTo(name);
        assertThat(appUserDTO.roles()).isEqualTo(List.of("ROLE_USER"));
    }
}
