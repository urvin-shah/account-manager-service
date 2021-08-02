package com.acmebank.service;

import com.acmebank.entity.Account;
import com.acmebank.model.AccountDetails;
import com.acmebank.repository.IAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger log = LoggerFactory.getLogger ( AccountService.class );

    /**
     * @author Urvin Shah
     * @apiNote getAccountDetails method return the AccountDetails based on accountNumber given.
     * @param accountNumber
     * @return
     */
    public Mono<AccountDetails> getAccountDetails(Long accountNumber) {
        log.debug("getAccountDetails() get initiated....");
        try {
            Optional<Account> optAccount = accountRepository.findById ( accountNumber );
            if (optAccount.isPresent ()) {
                Account account = optAccount.get ();
                log.debug("getAccountDetails() get completed....");
                return Mono.just ( AccountDetails.builder ()
                        .accountNumber ( account.getAccountNumber () )
                        .currency ( account.getCurrency () )
                        .accountBalance ( account.getAccountBalance () )
                        .build () );

            } else {
                log.error("Account number is not available in the database");
                throw new IllegalArgumentException ( String.format ( "Account number %d does not exist", accountNumber ) );
            }
        }catch (Exception e) {
            log.error("Account number can not be null.");
            throw new IllegalArgumentException ( String.format ( "Account number can not be null") );
        }
    }

    /**
     * @author Urvin Shah
     * @apiNote updateAccountBalance method update the accountBalance for the account number which has been provided.
     * @param accountNumber
     * @param accountBalance
     * @return
     */
    public Boolean updateAccountBalance(Long accountNumber,Double accountBalance) {
        log.debug("Account updation with the new balance initiated...");
        if(accountNumber != null) {
            Optional<Account> account = accountRepository.findById ( accountNumber );
            if(account.isPresent ()) {
                Account updateAccount = account.get ();
                updateAccount.setAccountBalance ( accountBalance );
                accountRepository.saveAndFlush ( updateAccount );
                log.debug("Account updation with the new balance completed successfully...");
                return Boolean.TRUE;
            } else {
                log.error("Account number is invalid");
            }
        } else {
            log.error("Account number can not be null.");
        }
        return Boolean.FALSE;
    }

    /**
     * @authod Urvin Shah
     * @apiNote getAccountByAccountNo method is retrieving the Account details of given accountNumber.
     * @param accountNumber
     * @return
     */
    public Mono<Account> getAccountByAccountNo(Long accountNumber) {
        if(accountNumber != null) {
            return Mono.just ( accountRepository.findById ( accountNumber ).orElseThrow (() -> new IllegalArgumentException ( String.format ( "Account number %d does not exist", accountNumber )))) ;
        }
       throw new IllegalArgumentException ("Account Number is Null");
    }
}
