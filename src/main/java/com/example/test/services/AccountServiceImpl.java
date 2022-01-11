package com.example.test.services;

import com.example.test.models.Account;
import com.example.test.models.Bank;
import com.example.test.repositories.AccountRepository;
import com.example.test.repositories.BankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    private BankRepository bankRepository;

    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Account findById(Long Id) {
        return accountRepository.findById(Id).orElseThrow();
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public int checkAllTransference(Long bankId) {
        Bank bank = bankRepository.findById(bankId).orElseThrow();
        return bank.getTotalTransaction();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal checkBalance(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        return account.getBalance();
    }

    @Override
    @Transactional
    public void transference(Long numAccountPayer, Long numAccountPayee, BigDecimal amount, Long bankId) {
        Account accountPayer = accountRepository.findById(numAccountPayer).orElseThrow();
        accountPayer.debit(amount);
        accountRepository.save(accountPayer);

        Account accountPayee = accountRepository.findById(numAccountPayee).orElseThrow();
        accountPayee.credit(amount);
        accountRepository.save(accountPayee);

        Bank bank = bankRepository.findById(bankId).orElseThrow();
        int totalTransaction = bank.getTotalTransaction();
        bank.setTotalTransaction(++totalTransaction);
        bankRepository.save(bank);


    }
}
