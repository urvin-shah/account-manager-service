package com.acmebank.service;

import com.acmebank.entity.Account;
import com.acmebank.model.AccountDetails;
import com.acmebank.repository.IAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith (MockitoExtension.class)
public class AccountServiceTest {

    @Autowired
    @InjectMocks
    private AccountService accountService;

    @Mock
    private IAccountRepository accountRepository;

    @Test
    @DisplayName ( "Get Account Details Success" )
    void testAccountDetails() {
        AccountDetails expected = AccountDetails.builder ()
                .accountNumber ( 88888888l )
                .currency ( "HKD" )
                .accountBalance ( 1000000.00 )
                .build ();
        Account account = Account.builder ()
                .accountNumber ( 88888888l )
                .currency ( "HKD" )
                .accountBalance ( 1000000.00 )
                .build ();
        doReturn( Optional.of ( account)).when(accountRepository).findById ( 88888888l );
        StepVerifier.create ( accountService.getAccountDetails (88888888l ) )
                .expectNext ( expected )
                .verifyComplete ();
    }

    @Test
    @DisplayName ( "Account number Does not exist" )
    void accountNumberNotExist() {
        assertThrows ( IllegalArgumentException.class,() -> {
            accountService.getAccountDetails ( 121212121l );
        } );
    }

    @Test
    @DisplayName ( "Update Account Balance Success" )
    void updateAccountBalanceSuccess() {
        Account account = Account.builder ()
                .accountNumber ( 88888888l )
                .currency ( "HKD" )
                .accountBalance ( 1000000.00 )
                .build ();
        doReturn( Optional.of ( account)).when(accountRepository).findById (88888888l );
        assertTrue ( accountService.updateAccountBalance ( 88888888l,900000.0 ));
    }

    @Test
    @DisplayName ( "Update Account balance with Invalid Account number" )
    void updateAccountBalanceWithInvalidAccountNo() {
        assertFalse ( accountService.updateAccountBalance ( 11111111l,152222.0 ) );
    }

    @Test
    @DisplayName ( "Update Account balance with Null Account number" )
    void updateAccountBalanceWithNullAccountNo() {
        assertFalse ( accountService.updateAccountBalance ( null,152222.0 ) );
    }

    @Test
    @DisplayName ( "Get Account Success" )
    void getAccountByAccountNo() {
        Account account = Account.builder ()
                .accountNumber ( 88888888l )
                .currency ( "HKD" )
                .accountBalance ( 1000000.00 )
                .build ();
        doReturn( Optional.of ( account)).when(accountRepository).findById (88888888l );
        StepVerifier.create ( accountService.getAccountByAccountNo ( 88888888l ) )
                .expectNext ( account )
                .verifyComplete ();
    }

    @Test
    @DisplayName ( "Get Account Doesn't exist" )
    void getAccountByAccountNoNotExist() {
        assertThrows ( IllegalArgumentException.class,() -> {
            accountService.getAccountByAccountNo ( 121212121l );
        } );
    }

    @Test
    @DisplayName ( "Get Account Number is null" )
    void getAccountByAccountNoIsNull() {
        assertThrows ( IllegalArgumentException.class,() -> {
            accountService.getAccountByAccountNo ( null );
        } );
    }
}