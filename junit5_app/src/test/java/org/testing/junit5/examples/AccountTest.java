package org.testing.junit5.examples;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void test_nameAccount() {
        Account account = new Account("Julian", new BigDecimal("1000.12345"));
        String expected = "Julian";
        assertEquals(expected, account.getPerson());
    }

    @Test
    void test_balanceAccount() {
        Account account = new Account("Julian", new BigDecimal("1000.12345"));
        assertEquals(1000.12345, account.getBalance().doubleValue());
        assertFalse(account.getBalance().compareTo(BigDecimal.ZERO) < 0);
    }
}