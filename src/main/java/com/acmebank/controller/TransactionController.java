package com.acmebank.controller;

import com.acmebank.model.AccountDetails;
import com.acmebank.model.BankTransfer;
import com.acmebank.service.AccountTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {

    @Autowired
    private AccountTransactionService accountTransactionService;

    Logger log = LoggerFactory.getLogger ( TransactionController.class );

    @PostMapping(value = "/banktransfer/{accountNumber}")
    public ResponseEntity<Mono<AccountDetails>> accountTransferTX(@PathVariable("accountNumber") Long accountNumber, @RequestBody BankTransfer bankTransfer) {
        log.info ( "TransactionController.accountTransferTX request initiated." );
        if(accountNumber != null) {
            bankTransfer.setFromAccountNumber ( accountNumber );
            Mono<AccountDetails> accountDetailsAfterTx = accountTransactionService.bankTransferTransaction ( bankTransfer );
            return ResponseEntity.ok (accountDetailsAfterTx);
        } else {
            throw new IllegalArgumentException("Bad Request, Invalid input");
        }

    }

}
