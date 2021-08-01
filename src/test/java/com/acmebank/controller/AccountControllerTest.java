package com.acmebank.controller;

import com.acmebank.model.AccountDetails;
import com.acmebank.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;


@WebFluxTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    AccountService accountService;

    @Test
    void getAccountDetails() {
        AccountDetails accountDetails = AccountDetails.builder ()
                .accountNumber ( 88888888l )
                .currency ( "HKD" )
                .accountBalance ( 1000000.00 )
                .build ();

        doReturn( Mono.just ( accountDetails )).when(accountService).getAccountDetails ( 88888888l );

        webTestClient.get ()
                .uri ( "/accounts/88888888" )
                .accept ( MediaType.APPLICATION_JSON )
                .exchange ()
                .expectStatus ()
                .isOk ()
                .expectBody (AccountDetails.class)
                .value ( accountDtls -> accountDtls.getAccountNumber (),equalTo(88888888l) );
    }
}