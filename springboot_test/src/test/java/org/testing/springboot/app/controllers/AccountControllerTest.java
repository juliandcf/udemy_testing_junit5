package org.testing.springboot.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testing.springboot.app.dtos.TransferDTO;
import org.testing.springboot.app.models.Account;
import org.testing.springboot.app.services.AccountService;
import org.testing.springboot.app.utils.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testDetail() throws Exception {
        when(accountService.findById(1l)).thenReturn(Data.createAccount001());

        mockMvc.perform(get("/api/accounts/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person").value("Andres"))
                .andExpect(jsonPath("$.balance").value("1000"));
    }

    @Test
    void testTransfer() throws Exception {
        TransferDTO dto = new TransferDTO();
        dto.setSourceAccountId(1l);
        dto.setDestinationAccountId(2l);
        dto.setAmount(new BigDecimal("100"));
        dto.setBankId(1l);

        mockMvc.perform(post("/api/accounts/transfer").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.transfer.sourceAccountId").value(1l));
    }

    @Test
    void testFindAll() throws Exception {
        List<Account> accounts = Arrays.asList(Data.createAccount001(), Data.createAccount002());
        when(accountService.findAll()).thenReturn(accounts);

        mockMvc.perform(get("/api/accounts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].person").value("Andres"))
                .andExpect(jsonPath("$.[0].balance").value("1000"))
                .andExpect(jsonPath("$.[1].person").value("Jhon"))
                .andExpect(jsonPath("$.[1].balance").value("2000"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testSave() throws Exception {
        Account account = new Account(null, "Pepe", new BigDecimal("3000"));
        when(accountService.save(any())).thenReturn(account);

        mockMvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person").value("Pepe"))
                .andExpect(jsonPath("$.balance").value("3000"));
        verify(accountService).save(any());
    }
}