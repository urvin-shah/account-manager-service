package com.acmebank.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Builder
@Entity
public class Account {
    @Id
    private Long accountNumber;
    private Double accountBalance;
    private String currency;

    Account(){}
}
