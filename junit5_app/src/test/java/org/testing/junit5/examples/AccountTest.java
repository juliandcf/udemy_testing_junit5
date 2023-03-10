package org.testing.junit5.examples;

import org.junit.jupiter.api.Test;
import org.testing.junit5.examples.exceptions.NotEnoughMoneyException;
import org.testing.junit5.examples.models.Account;
import org.testing.junit5.examples.models.Bank;

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

    @Test
    void testAccountReference() {
        Account account = new Account("Jonh Doe", new BigDecimal("8900.9997"));
        Account account2 = new Account("Jonh Doe", new BigDecimal("8900.9997"));
        assertEquals(account, account2);
    }

    @Test
    void test_accountDebit() {
        Account account = new Account("Julian", new BigDecimal("1000.12345"));
        account.debit(new BigDecimal(100));
        assertNotNull(account.getBalance());
        assertEquals(900, account.getBalance().intValue());
        assertEquals("900.12345", account.getBalance().toPlainString());
    }

    @Test
    void test_accountCredit() {
        Account account = new Account("Julian", new BigDecimal("1000.12345"));
        account.credit(new BigDecimal(100));
        assertNotNull(account.getBalance());
        assertEquals(1100, account.getBalance().intValue());
        assertEquals("1100.12345", account.getBalance().toPlainString());
    }

    @Test
    void test_notEnoughMoneyException() {
        Account account = new Account("Julian", new BigDecimal("1000.12345"));
        Exception exception = assertThrows(NotEnoughMoneyException.class, () -> {
            account.debit(new BigDecimal("2000"));
        });
        String messageExpected = "There is not enough money in the account";
        assertEquals(messageExpected, exception.getMessage());
    }

    @Test
    void test_transferMoneyAccounts() {
        Account account1 = new Account("Jhon Doe", new BigDecimal("1500"));
        Account account2 = new Account("Julian", new BigDecimal("2500"));

        Bank bank = new Bank("Fake Bank");
        bank.transfer(account1, account2, new BigDecimal(500));
        assertEquals("3000", account2.getBalance().toPlainString());
        assertEquals("1000", account1.getBalance().toPlainString());
    }

    @Test
    void test_relationBankAccount() {
        Account account1 = new Account("Jhon Doe", new BigDecimal("1500"));
        Account account2 = new Account("Julian", new BigDecimal("2500"));

        Bank bank = new Bank("Fake Bank");
        bank.addAccount(account1);
        bank.addAccount(account2);

        bank.transfer(account1, account2, new BigDecimal(500));
        assertEquals("3000", account2.getBalance().toPlainString());
        assertEquals("1000", account1.getBalance().toPlainString());

        assertEquals(2, bank.getAccounts().size());
        assertEquals("Fake Bank", account1.getBank().getName());
        assertEquals("Julian", bank.getAccounts().stream()
                .filter(account -> account.getPerson().equals("Julian"))
                .findFirst()
                .get()
                .getPerson());

        assertTrue(bank.getAccounts().stream()
                .filter(account -> account.getPerson().equals("Julian"))
                .findFirst()
                .isPresent());

        assertTrue(bank.getAccounts().stream()
                .anyMatch(account -> account.getPerson().equals("Julian")));

        assertTrue(bank.getAccounts().stream()
                .anyMatch(account -> account.getPerson().equals("Jhon Doe")));
    }
}