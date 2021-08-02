package com.acmebank.controller;

import com.acmebank.model.AccountDetails;
import com.acmebank.model.BankTransfer;
import com.acmebank.service.AccountTransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.doReturn;

@WebFluxTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    AccountTransactionService accountTransactionService;

    @Test
    @DisplayName ( "Account Transfer Success" )
    void accountTransferTX() {

        BankTransfer bankTransfer = BankTransfer.builder ()
                .fromAccountNumber ( 88888888l )
                .toAccountNumber ( 12345678l )
                .transferAmount ( 100000.0 )
                .build ();

        AccountDetails accountDetails = AccountDetails.builder ()
                .accountNumber ( 88888888l )
                .currency ( "HKD" )
                .accountBalance ( 900000.00 )
                .build ();

        doReturn( Mono.just ( accountDetails )).when(accountTransactionService).bankTransferTransaction ( bankTransfer );

        webTestClient.post ()
                .uri ( "/transaction/banktransfer/88888888" )
                .contentType ( MediaType.APPLICATION_JSON )
                .accept ( MediaType.APPLICATION_JSON )
                .body ( Mono.just(bankTransfer),BankTransfer.class )
                .exchange ()
                .expectStatus ()
                .isOk ();
    }
}