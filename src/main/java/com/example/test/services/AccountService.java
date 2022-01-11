package com.example.test.services;

import com.example.test.models.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    List<Account> findAll();

    Account findById(Long Id);

    Account save(Account account);

    int checkAllTransference(Long bankId);

    BigDecimal checkBalance(Long accountId);

    void transference(Long numAccountPayer,Long numAccountPayee,BigDecimal amount, Long bankId);
}
