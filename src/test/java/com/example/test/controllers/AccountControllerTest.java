package com.example.test.controllers;

import com.example.test.Data;
import com.example.test.models.Account;
import com.example.test.models.Transaction;
import com.example.test.services.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService accountService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testDetail() throws Exception{
        //Given
        when(accountService.findById(1L)).thenReturn(Data.createAccount001().orElseThrow());

        //when
        mvc.perform(get("/api/accounts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person").value("Nacho"))
                .andExpect(jsonPath("$.balance").value("1000"));

        verify(accountService).findById(1L);
    }

    @Test
    void testTransfer() throws Exception {

        Transaction transaction = new Transaction();
        transaction.setAccountPayer(1L);
        transaction.setAccountPayee(2L);
        transaction.setBankId(1L);

        //When
        mvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaction)))

        //Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.message").value("Transfer successfully done"))
                .andExpect(jsonPath("$.transaction.accountPayer").value(1L));
    }

    @Test
    void testList() throws Exception {
        //Given
        List<Account> accounts = Arrays.asList(Data.createAccount001().orElseThrow(), Data.createAccount002().orElseThrow());
        when(accountService.findAll()).thenReturn(accounts);

        //When
        mvc.perform(get("/api/accounts").contentType(MediaType.APPLICATION_JSON))

        //Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].person").value("Nacho"))
                .andExpect(jsonPath("$[0].balance").value("1000"))
                .andExpect(jsonPath("$[1].person").value("Luis"))
                .andExpect(jsonPath("$[1].balance").value("2000"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(accounts)));
        verify(accountService).findAll();
    }

    @Test
    void testSave() throws Exception {
        //Given
        Account account = new Account(null, "Pepe", new BigDecimal(5000));
        when(accountService.save(any())).then(invocation -> {
            Account a = invocation.getArgument(0);
            a.setId(3L);
            return a;
        });

        //When
        mvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))

        //Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.person", is("Pepe")))
                .andExpect(jsonPath("$.balance", is(5000)));
        verify(accountService).save(any());

    }
}