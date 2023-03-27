package org.testing.springboot.app.controllers;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testing.springboot.app.dtos.TransferDTO;
import org.testing.springboot.app.models.Account;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerWebClientTest {

    @Autowired
    private WebTestClient client;

    @Test
    @Order(2)
    void testTransfer() {
        TransferDTO dto = new TransferDTO();
        dto.setSourceAccountId(1l);
        dto.setDestinationAccountId(2l);
        dto.setAmount(new BigDecimal("100"));
        dto.setBankId(1l);

        client.post().uri("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.transfer.sourceAccountId").isEqualTo(1l);
    }

    @Test
    @Order(1)
    void testDetail() {
        client.get().uri("/api/accounts/1").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.person").isEqualTo("Andrés")
                .jsonPath("$.balance").isEqualTo("1000.0");
    }

    @Test
    @Order(3)
    void testList() {
        client.get().uri("/api/accounts").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Account.class)
                .consumeWith(response -> {
                    List<Account> accounts = response.getResponseBody();
                    assertEquals(2, accounts.size());
                })
                .hasSize(2);
    }

}