package com.acmebank.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long txId;

    private Long accountNumber;
    private String transactionType;
    private Double transactionAmount;
    private LocalDateTime transactionTime;
    private Double balance;

    Transaction(){}
}
