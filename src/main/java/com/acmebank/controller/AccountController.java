package com.acmebank.controller;

import com.acmebank.model.AccountDetails;
import com.acmebank.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    Logger log = LoggerFactory.getLogger ( AccountController.class );

    @GetMapping(value = "/{accountNumber}")
    public ResponseEntity<Mono<AccountDetails>> getAccountDetails(@PathVariable("accountNumber") Long accountNumber){
        log.info ( "AccountController.getAccountDetails request initiated." );
        Mono<AccountDetails> accountDetails = accountService.getAccountDetails ( accountNumber );
        HttpStatus status = accountDetails != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<Mono<AccountDetails>>(accountDetails, status);
    }
}
