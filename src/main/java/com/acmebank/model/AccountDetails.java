package com.acmebank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AccountDetails {
    private Long accountNumber;
    private Double accountBalance;
    private String currency;
    AccountDetails(){}
}
