package com.example.test.repositories;

import com.example.test.models.Account;
import com.example.test.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    //List<Account> findAll();

    //Bank findById(Long Id);

    //void update(Bank bank);
}
