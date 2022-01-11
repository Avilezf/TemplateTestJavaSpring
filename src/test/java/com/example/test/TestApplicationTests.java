package com.example.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.test.exception.InsufficientMoneyException;
import com.example.test.models.Account;
import com.example.test.models.Bank;
import com.example.test.repositories.AccountRepository;
import com.example.test.repositories.BankRepository;
import com.example.test.services.AccountService;
import com.example.test.services.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

@SpringBootTest
class TestApplicationTests {

    @MockBean
    AccountRepository accountRepository;

    @MockBean
    BankRepository bankRepository;

    @Autowired
    AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        //accountRepository = mock(AccountRepository.class);
        //bankRepository = mock(BankRepository.class);
        //accountService = new AccountServiceImpl(accountRepository, bankRepository);
    }

    @Test
    void contextLoads() {
        when(accountRepository.findById(1L)).thenReturn(Data.createAccount001());
        when(accountRepository.findById(2L)).thenReturn(Data.createAccount002());
        when(bankRepository.findById(1L)).thenReturn(Data.createBank());

        BigDecimal balancePayer = accountService.checkBalance(1L);
        BigDecimal balancePayee = accountService.checkBalance(2L);
        assertEquals("1000", balancePayer.toPlainString());
        assertEquals("2000", balancePayee.toPlainString());

        accountService.transference(1L, 2L, new BigDecimal("100"), 1L);

        balancePayer = accountService.checkBalance(1L);
        balancePayee = accountService.checkBalance(2L);
        assertEquals("900", balancePayer.toPlainString());
        assertEquals("2100", balancePayee.toPlainString());

        int total = accountService.checkAllTransference(1L);
        assertEquals(1, total);

        verify(accountRepository, times(3)).findById(1L);
        verify(accountRepository, times(3)).findById(2L);
        verify(accountRepository, times(2)).save(any(Account.class));

        verify(bankRepository, times(2)).findById(1L);
        verify(bankRepository).save(any(Bank.class));

        verify(accountRepository, times(6)).findById(anyLong());
        verify(accountRepository, never()).findAll();
    }


    @Test
    void contextLoads2() {
        when(accountRepository.findById(1L)).thenReturn(Data.createAccount001());
        when(accountRepository.findById(2L)).thenReturn(Data.createAccount002());
        when(bankRepository.findById(1L)).thenReturn(Data.createBank());

        BigDecimal balancePayer = accountService.checkBalance(1L);
        BigDecimal balancePayee = accountService.checkBalance(2L);
        assertEquals("1000", balancePayer.toPlainString());
        assertEquals("2000", balancePayee.toPlainString());

        assertThrows(InsufficientMoneyException.class, () -> {
            accountService.transference(1L, 2L, new BigDecimal("1200"), 1L);
        });

        balancePayer = accountService.checkBalance(1L);
        balancePayee = accountService.checkBalance(2L);
        assertEquals("1000", balancePayer.toPlainString());
        assertEquals("2000", balancePayee.toPlainString());

        int total = accountService.checkAllTransference(1L);
        assertEquals(0, total);

        verify(accountRepository, times(3)).findById(1L);
        verify(accountRepository, times(2)).findById(2L);
        verify(accountRepository, never()).save(any(Account.class));

        verify(bankRepository, times(1)).findById(1L);
        verify(bankRepository, never()).save(any(Bank.class));

        verify(accountRepository, times(5)).findById(anyLong());
        verify(accountRepository, never()).findAll();
    }

    @Test
    void contextLoads3() {
        when(accountRepository.findById(1L)).thenReturn(Data.createAccount001());

        Account account1 = accountService.findById(1L);
        Account account2 = accountService.findById(1L);

        assertSame(account1, account2);
    }
}
