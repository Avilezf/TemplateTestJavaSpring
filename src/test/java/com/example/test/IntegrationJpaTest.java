package com.example.test;

import com.example.test.models.Account;
import com.example.test.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IntegrationJpaTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    void testFindById(){
        Optional<Account> account = accountRepository.findById(1L);
        assertTrue(account.isPresent());
        assertEquals("Nacho", account.orElseThrow().getPerson());
    }

    @Test
    void testFindByPerson(){
        Optional<Account> account = accountRepository.findByPerson("Nacho");
        assertTrue(account.isPresent());
        assertEquals("Nacho", account.orElseThrow().getPerson());
        assertEquals("1000.00", account.orElseThrow().getBalance().toPlainString());
    }

    @Test
    void testFindByPersonThrowException(){
        Optional<Account> account = accountRepository.findByPerson("Bob");
        assertThrows(NoSuchElementException.class, account::orElseThrow);
        assertFalse(account.isPresent());
    }

    @Test
    void testFindAll(){
        List<Account> accounts = accountRepository.findAll();
        assertFalse(accounts.isEmpty());
        assertEquals(4, accounts.size());
    }

    @Test
    void testSave(){
        //Given
        Account savingAccount = new Account(null, "Pepe", new BigDecimal("3000"));
        Account save = accountRepository.save(savingAccount);

        //When
        Account account = accountRepository.findById(save.getId()).orElseThrow();

        //Then
        assertEquals("Pepe", account.getPerson());
        assertEquals("3000", account.getBalance().toPlainString());
        assertEquals(5, account.getId());
    }

    @Test
    void testUpdate(){
        //Given
        Account savedAccount = new Account(null, "Pepe", new BigDecimal("3000"));

        //When
        Account account = accountRepository.save(savedAccount);

        //Then
        assertEquals("Pepe", account.getPerson());
        assertEquals("3000", account.getBalance().toPlainString());

        account.setBalance(new BigDecimal("3800"));
        Account updatedAccount = accountRepository.save(account);

        assertEquals("Pepe", updatedAccount.getPerson());
        assertEquals("3800", updatedAccount.getBalance().toPlainString());
    }

    @Test
    void testDelete(){
        Account account = accountRepository.findById(2L).orElseThrow();
        assertEquals("Luis", account.getPerson());

        accountRepository.delete(account);

        assertThrows(NoSuchElementException.class, () -> {
            accountRepository.findById(2L).orElseThrow();
        });
        assertEquals(3, accountRepository.findAll().size());
    }
}
