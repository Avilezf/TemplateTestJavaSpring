package com.example.test.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "banks")
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "total_transaction")
    private int totalTransaction;

    public Bank(){
    }

    public Bank(Long id, String name, int totalTransaction) {
        this.id = id;
        this.name = name;
        this.totalTransaction = totalTransaction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalTransaction() {
        return totalTransaction;
    }

    public void setTotalTransaction(int totalTransaction) {
        this.totalTransaction = totalTransaction;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return totalTransaction == bank.totalTransaction && Objects.equals(id, bank.id) && Objects.equals(name, bank.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, totalTransaction);
    }
}
