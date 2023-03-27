package org.testing.springboot.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testing.springboot.app.dtos.TransferDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerRestTemplateTest {

    @Autowired
    private TestRestTemplate client;

    private ObjectMapper objectMapper;

    // Port execution
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testTransfer() throws JsonProcessingException {
        TransferDTO dto = new TransferDTO();
        dto.setSourceAccountId(1l);
        dto.setDestinationAccountId(2l);
        dto.setAmount(new BigDecimal("100"));
        dto.setBankId(1l);

        ResponseEntity<String> response = client.postForEntity("/api/accounts/transfer", dto, String.class);

        String json = response.getBody();

        // Json node to navigate Json
        JsonNode jsonNode = objectMapper.readTree(json);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
        assertEquals("100", jsonNode.path("transfer").path("amount").asText());

    }
}