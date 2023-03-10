package org.testing.junit5.examples;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
class AccountTest {

    @Test
    void test_nameAccount() {
        Account account = new Account("Julian", BigDecimal.valueOf(123.12));
        String expected = "Julian";
        assertEquals(expected, account.getPerson());
    }
}