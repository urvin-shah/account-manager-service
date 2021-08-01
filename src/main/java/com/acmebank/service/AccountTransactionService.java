package com.acmebank.service;

import com.acmebank.entity.Account;
import com.acmebank.entity.Transaction;
import com.acmebank.model.AccountDetails;
import com.acmebank.model.BankTransfer;
import com.acmebank.model.TransactionType;
import com.acmebank.repository.ITransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class AccountTransactionService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ITransactionRepository transactionRepository;

    public Mono<AccountDetails> bankTransferTransaction(BankTransfer bankTransfer) {
        if(bankTransfer != null) {
            // Fetch From & To account using the fromAccountNumber and toAccountNumber
            Account fromAccount = accountService.getAccountByAccountNo ( bankTransfer.getFromAccountNumber () ).block ();
            Account toAccount = accountService.getAccountByAccountNo ( bankTransfer.getToAccountNumber () ).block ();

            if(fromAccount != null ) {
                Double nextFromAccountBalance = fromAccount.getAccountBalance () - bankTransfer.getTransferAmount ();
                if (toAccount != null) {
                    Double nextToAccountBalance = toAccount.getAccountBalance () + bankTransfer.getTransferAmount ();
                    if (bankTransfer.getTransferAmount () >0 && fromAccount.getAccountBalance () >= bankTransfer.getTransferAmount ()) {
                        Transaction fromAcctTransaction = Transaction.builder ()
                                .accountNumber ( bankTransfer.getFromAccountNumber () )
                                .transactionType ( TransactionType.WITHDRAW.toString () )
                                .transactionAmount ( bankTransfer.getTransferAmount () )
                                .balance ( nextFromAccountBalance )
                                .build ();

                        Transaction toAcctTransaction = Transaction.builder ()
                                .accountNumber ( bankTransfer.getFromAccountNumber () )
                                .transactionType ( TransactionType.DEPOSIT.toString () )
                                .transactionAmount ( bankTransfer.getTransferAmount () )
                                .balance ( nextToAccountBalance )
                                .build ();

                        fromAccount.setAccountBalance ( nextFromAccountBalance );
                        toAccount.setAccountBalance ( nextToAccountBalance );
                        List<Transaction> transactions = Arrays.asList ( fromAcctTransaction, toAcctTransaction );
                        List<Account> accounts = Arrays.asList ( fromAccount, toAccount );

                        transactionRepository.saveAll ( transactions );
                        accountService.updateAccountBalance ( bankTransfer.getFromAccountNumber (), nextFromAccountBalance );
                        accountService.updateAccountBalance ( bankTransfer.getToAccountNumber (), nextToAccountBalance );
                        return Mono.just ( AccountDetails.builder ()
                                .accountNumber ( fromAccount.getAccountNumber () )
                                .accountBalance ( fromAccount.getAccountBalance () )
                                .currency ( fromAccount.getCurrency () )
                                .build () );
                    } else {
                        throw new IllegalArgumentException ("Invalid transaction amount...");
                    }
                } else {
                    throw new IllegalArgumentException ("Invalid to account number...");
                }
            } else {
                throw new IllegalArgumentException ("Invalid from Account number...");
            }
        }
        throw new IllegalArgumentException ("Invalid Input");
    }
}
