package com.acmebank.service;

import com.acmebank.entity.Account;
import com.acmebank.model.AccountDetails;
import com.acmebank.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class AccountService {

    @Autowired
    private IAccountRepository accountRepository;

    public Mono<AccountDetails> getAccountDetails(Long accountNumber) {
        try {
            Optional<Account> optAccount = accountRepository.findById ( accountNumber );
            if (optAccount.isPresent ()) {
                Account account = optAccount.get ();
                return Mono.just ( AccountDetails.builder ()
                        .accountNumber ( account.getAccountNumber () )
                        .currency ( account.getCurrency () )
                        .accountBalance ( account.getAccountBalance () )
                        .build () );
            } else {
                throw new IllegalArgumentException ( String.format ( "Account number %d does not exist", accountNumber ) );
            }
        }catch (Exception e) {
            throw new IllegalArgumentException ( String.format ( "Account number %d does not exist", accountNumber ) );
        }
    }

    public Boolean updateAccountBalance(Long accountNumber,Double accountBalance) {
        if(accountNumber != null) {
            Optional<Account> account = accountRepository.findById ( accountNumber );
            if(account.isPresent ()) {
                Account updateAccount = account.get ();
                updateAccount.setAccountBalance ( accountBalance );
                accountRepository.saveAndFlush ( updateAccount );
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public Mono<Account> getAccountByAccountNo(Long accountNumber) {
        if(accountNumber != null) {
            return Mono.just ( accountRepository.findById ( accountNumber ).orElseThrow (() -> new IllegalArgumentException ( String.format ( "Account number %d does not exist", accountNumber )))) ;
        }
       throw new IllegalArgumentException ("Account Number is Null");
    }
}
