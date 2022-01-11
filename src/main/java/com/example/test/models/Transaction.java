package com.example.test.models;

import java.math.BigDecimal;

public class Transaction {
    private Long accountPayer;
    private Long accountPayee;
    private BigDecimal amount;
    private Long bankId;

    public Long getAccountPayer() {
        return accountPayer;
    }

    public void setAccountPayer(Long accountPayer) {
        this.accountPayer = accountPayer;
    }

    public Long getAccountPayee() {
        return accountPayee;
    }

    public void setAccountPayee(Long accountPayee) {
        this.accountPayee = accountPayee;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }
}
