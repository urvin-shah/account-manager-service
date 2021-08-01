package com.acmebank.service;

import com.acmebank.entity.Account;
import com.acmebank.model.AccountDetails;
import com.acmebank.model.BankTransfer;
import com.acmebank.repository.IAccountRepository;
import com.acmebank.repository.ITransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@Import ( AccountTransactionService.class )
public class AccountTransactionServiceTest {

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private IAccountRepository accountRepository;

    @InjectMocks
    private AccountTransactionService accountTransactionService;

    @Mock
    private AccountService accountService;

    @Test
    @DisplayName ( "Bank Transfer Success" )
    void bankTransferTransaction() {
        Account fromAccount = Account.builder ()
                .accountNumber ( 88888888l )
                .currency ( "HKD" )
                .accountBalance ( 1000000.00 )
                .build ();
        doReturn( Mono.just ( fromAccount)).when(accountService).getAccountByAccountNo ( 88888888l );

        Account toAccount = Account.builder ()
                .accountNumber ( 12345678l )
                .currency ( "HKD" )
                .accountBalance ( 1000000.00 )
                .build ();
        doReturn( Mono.just(toAccount)).when(accountService).getAccountByAccountNo ( 12345678l );

        BankTransfer bankTransfer = BankTransfer.builder ()
                .fromAccountNumber ( 88888888l )
                .toAccountNumber ( 12345678l )
                .transferAmount ( 100000.0 )
                .build ();

        AccountDetails expectedFromAccountDetails = AccountDetails.builder ()
                .accountNumber ( 88888888l )
                .currency ( "HKD" )
                .accountBalance ( 900000.0 )
                .build ();
        StepVerifier.create ( accountTransactionService.bankTransferTransaction (bankTransfer) )
                .expectNext ( expectedFromAccountDetails )
                .verifyComplete ();
    }

    @Test
    @DisplayName ( "Bank Transfer Failure" )
    void bankTransferTransactionFailure() {
        assertThrows ( IllegalArgumentException.class,() -> {
            accountTransactionService.bankTransferTransaction ( null );
        } );

        BankTransfer bankTransferWithNullFromAccNo = BankTransfer.builder ()
                .fromAccountNumber ( null )
                .toAccountNumber ( 12345678l )
                .transferAmount ( 100000.0 )
                .build ();

        BankTransfer bankTransferWithNullToAccNo = BankTransfer.builder ()
                .fromAccountNumber ( 88888888l )
                .toAccountNumber ( null )
                .transferAmount ( 100000.0 )
                .build ();

        assertThrows ( NullPointerException.class,() -> {
            accountTransactionService.bankTransferTransaction ( bankTransferWithNullFromAccNo );
        } );

        Account fromAccount = Account.builder ()
                .accountNumber ( 88888888l )
                .currency ( "HKD" )
                .accountBalance ( 1000000.00 )
                .build ();
        doReturn( Mono.just ( fromAccount)).when(accountService).getAccountByAccountNo ( 88888888l );

        assertThrows ( NullPointerException.class, () -> {
            accountTransactionService.bankTransferTransaction ( bankTransferWithNullToAccNo );
        } );

        Account toAccount = Account.builder ()
                .accountNumber ( 12345678l )
                .currency ( "HKD" )
                .accountBalance ( 1000000.00 )
                .build ();
        doReturn( Mono.just(toAccount)).when(accountService).getAccountByAccountNo ( 12345678l );


        BankTransfer bankTransferWithNegativeAmount = BankTransfer.builder ()
                .fromAccountNumber ( 12345678l )
                .toAccountNumber ( 88888888l )
                .transferAmount ( -100000.0 )
                .build ();
        assertThrows ( IllegalArgumentException.class,()->{
            accountTransactionService.bankTransferTransaction ( bankTransferWithNegativeAmount );
        } );

        BankTransfer bankTransferWithGreaterThenAccountBalance = BankTransfer.builder ()
                .fromAccountNumber ( 12345678l )
                .toAccountNumber ( 88888888l )
                .transferAmount ( 10000000.0 )
                .build ();
        assertThrows ( IllegalArgumentException.class,()->{
            accountTransactionService.bankTransferTransaction ( bankTransferWithGreaterThenAccountBalance );
        } );

    }
}