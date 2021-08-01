package com.acmebank.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankTransfer {
    private Long fromAccountNumber;
    private Long toAccountNumber;
    private Double transferAmount;
}
