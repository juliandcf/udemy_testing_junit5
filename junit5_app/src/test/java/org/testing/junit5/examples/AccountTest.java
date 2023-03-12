package org.testing.junit5.examples;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;
import org.testing.junit5.examples.exceptions.NotEnoughMoneyException;
import org.testing.junit5.examples.models.Account;
import org.testing.junit5.examples.models.Bank;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    Account account;

    @BeforeEach
    void beforeEach() {
        this.account = new Account("Julian", new BigDecimal("1000.12345"));
    }

    @Test
    @DisplayName("Test that the name is set correctly in the account")
    void test_nameAccount() {
        String expected = "Julian";
        assertEquals(expected, account.getPerson(), () -> "the name must be equals");
    }

    @Test
    @DisplayName("Test that the balance is set correctly in the account")
    void test_balanceAccount() {
        assertEquals(1000.12345, account.getBalance().doubleValue());
        assertFalse(account.getBalance().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    @DisplayName("Test account equals")
    void testAccountReference() {
        Account account = new Account("Jonh Doe", new BigDecimal("8900.9997"));
        Account account2 = new Account("Jonh Doe", new BigDecimal("8900.9997"));
        assertEquals(account, account2);
    }

    @Test
    @DisplayName("Test account debit money")
    void test_accountDebit() {
        account.debit(new BigDecimal(100));
        assertNotNull(account.getBalance());
        assertEquals(900, account.getBalance().intValue());
        assertEquals("900.12345", account.getBalance().toPlainString());
    }

    @Test
    @DisplayName("Test account credit money")
    void test_accountCredit() {
        account.credit(new BigDecimal(100));
        assertNotNull(account.getBalance());
        assertEquals(1100, account.getBalance().intValue());
        assertEquals("1100.12345", account.getBalance().toPlainString());
    }

    @Test
    @DisplayName("Tests the case of a debit greater than the amount of money in the account")
    void test_notEnoughMoneyException() {
        Exception exception = assertThrows(NotEnoughMoneyException.class, () -> {
            account.debit(new BigDecimal("2000"));
        });
        String messageExpected = "There is not enough money in the account";
        assertEquals(messageExpected, exception.getMessage());
    }

    @Test
    @DisplayName("Tests transfers between accounts with the bank")
    void test_transferMoneyAccounts() {
        Account account1 = new Account("Jhon Doe", new BigDecimal("1500"));
        Account account2 = new Account("Julian", new BigDecimal("2500"));

        Bank bank = new Bank("Fake Bank");
        bank.transfer(account1, account2, new BigDecimal(500));
        assertEquals("3000", account2.getBalance().toPlainString());
        assertEquals("1000", account1.getBalance().toPlainString());
    }

    @Test
    @DisplayName("Tests bank relations with accounts")
    void test_relationBankAccount() {
        Account account1 = new Account("Jhon Doe", new BigDecimal("1500"));
        Account account2 = new Account("Julian", new BigDecimal("2500"));

        Bank bank = new Bank("Fake Bank");
        bank.addAccount(account1);
        bank.addAccount(account2);

        bank.transfer(account1, account2, new BigDecimal(500));

        assertAll(
                () -> assertEquals("3000", account2.getBalance().toPlainString()),
                () -> assertEquals("1000", account1.getBalance().toPlainString()),
                () -> assertEquals(2, bank.getAccounts().size()),
                () -> assertEquals("Fake Bank", account1.getBank().getName()),
                () -> assertEquals("Julian", bank.getAccounts().stream()
                        .filter(account -> account.getPerson().equals("Julian"))
                        .findFirst()
                        .get()
                        .getPerson()),
                () -> assertTrue(bank.getAccounts().stream()
                        .filter(account -> account.getPerson().equals("Julian"))
                        .findFirst()
                        .isPresent()),
                () -> assertTrue(bank.getAccounts().stream()
                        .anyMatch(account -> account.getPerson().equals("Julian"))),
                () -> assertTrue(bank.getAccounts().stream()
                        .anyMatch(account -> account.getPerson().equals("Jhon Doe")))
        );
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testOnlyWindows(){

    }

    @Test
    @EnabledOnOs(OS.LINUX)
    void testOnlyLinux(){

    }

    @Test
    void printSystemProperties() {
        Properties properties = System.getProperties();
        properties.forEach((k,v) -> System.out.println(k + ":" + v));
    }

    @Test
    void printEnvVariables() {
        Map<String, String> envs = System.getenv();
        envs.forEach((k,v) -> System.out.println(k + ":" + v));
    }

    @Test
    @EnabledIfSystemProperty(named = "java.version", matches = "17.0.2")
    void testJavaVersionProperties() {

    }

    @Test
    @EnabledIfEnvironmentVariable(named = "DESKTOP_SESSION", matches = "ubuntu")
    void testDesktopSessionEnv() {

    }
}