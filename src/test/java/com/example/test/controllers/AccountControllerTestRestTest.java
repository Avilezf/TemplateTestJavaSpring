package com.example.test.controllers;

import com.example.test.models.Account;
import com.example.test.models.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.print.attribute.standard.Media;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTestRestTest {

    @Autowired
    private TestRestTemplate client;

    @LocalServerPort
    private int port;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void transferTest() throws JsonProcessingException {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100"));
        transaction.setAccountPayer(1L);
        transaction.setAccountPayee(2L);
        transaction.setBankId(1L);

        ResponseEntity<String> responseEntity = client.postForEntity(Uri("/api/accounts/transfer"), transaction, String.class);
        String json = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertNotNull(json);
        assertTrue(json.contains("Transfer successfully done"));

        JsonNode jsonNode = objectMapper.readTree(json);
        assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
    }

    @Test
    @Order(2)
    void detailTest(){
        ResponseEntity<Account> response = client.getForEntity(Uri("/api/accounts/1"), Account.class);
        Account account = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        assertNotNull(account);
        assertEquals("Nacho", account.getPerson());
        assertEquals("900.00", account.getBalance().toPlainString());
        assertEquals(new Account(1L, "Nacho", new BigDecimal("900.00")), account);
    }

    @Test
    @Order(3)
    void listTest(){
        ResponseEntity<Account[]> response =  client.getForEntity(Uri("/api/accounts"), Account[].class);
        List<Account> accounts = Arrays.asList(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        assertNotNull(accounts);
        assertEquals(4, accounts.size());
        assertEquals(1L, accounts.get(0).getId());
        assertEquals("Nacho", accounts.get(0).getPerson());
        assertEquals("900.00", accounts.get(0).getBalance().toPlainString());
        assertEquals(2L, accounts.get(1).getId());
        assertEquals("Luis", accounts.get(1).getPerson());
        assertEquals("2100.00", accounts.get(1).getBalance().toPlainString());
    }

    private String Uri(String uri){
        return "http://localhost:"+port+ uri;
    }
}