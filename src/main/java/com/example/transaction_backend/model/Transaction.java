package com.example.transaction_backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {

    private LocalDate transactionDate;
    private String accountNumber;
    private String accountHolderName;
    private BigDecimal amount;
    private String status;

    public Transaction() {
    }

    public Transaction(
            LocalDate transactionDate,
            String accountNumber,
            String accountHolderName,
            BigDecimal amount,
            String status) {

        this.transactionDate = transactionDate;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.amount = amount;
        this.status = status;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
