package com.example.test;

import com.example.test.models.Account;
import com.example.test.models.Bank;

import java.math.BigDecimal;
import java.util.Optional;

public class Data {

    public static Optional<Account> createAccount001(){
        return Optional.of(new Account(1L, "Nacho", new BigDecimal("1000")));
    }

    public static Optional<Account> createAccount002(){
        return Optional.of( new Account(2L, "Luis", new BigDecimal("2000")));
    }

    public static Optional<Account> createAccount003(){
        return Optional.of(new Account(3L, "Ana", new BigDecimal("3000")));
    }

    public static Optional<Account> createAccount004() {
        return Optional.of(new Account(4L, "Karen", new BigDecimal("4000")));
    }

    public static Optional<Bank> createBank() {
        return Optional.of(new Bank(1L, "SapBank", 0));
    }

}
