package org.testing.junit5.examples;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.testing.junit5.examples.exceptions.NotEnoughMoneyException;
import org.testing.junit5.examples.models.Account;
import org.testing.junit5.examples.models.Bank;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class AccountTest {

    Account account;

    @BeforeEach
    void beforeEach() {
        this.account = new Account("Julian", new BigDecimal("1000.12345"));
    }

    @Nested
    class AccountPropertiesTest {
        @Test
        @DisplayName("Test that the name is set correctly in the account")
        void test_nameAccount(TestInfo testInfo, TestReporter testReporter) {
            testReporter.publishEntry("preuba test report: " + testInfo.getDisplayName());
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

    }

    @Nested
    class AccountOperationsTest {

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
    }

    @Nested
    class BankAccountRelationsTest {

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

            assertAll(() -> assertEquals("3000", account2.getBalance().toPlainString()), () -> assertEquals("1000", account1.getBalance().toPlainString()), () -> assertEquals(2, bank.getAccounts().size()), () -> assertEquals("Fake Bank", account1.getBank().getName()), () -> assertEquals("Julian", bank.getAccounts().stream().filter(account -> account.getPerson().equals("Julian")).findFirst().get().getPerson()), () -> assertTrue(bank.getAccounts().stream().filter(account -> account.getPerson().equals("Julian")).findFirst().isPresent()), () -> assertTrue(bank.getAccounts().stream().anyMatch(account -> account.getPerson().equals("Julian"))), () -> assertTrue(bank.getAccounts().stream().anyMatch(account -> account.getPerson().equals("Jhon Doe"))));
        }
    }


    @Nested
    class OsTest {

        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testOnlyWindows() {
        }

        @Test
        @EnabledOnOs(OS.LINUX)
        void testOnlyLinux() {

        }
    }

    @Nested
    class SystemPropertiesTest {

        @Test
        void printSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((k, v) -> System.out.println(k + ":" + v));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = "17.0.2")
        void testJavaVersionProperties() {

        }

    }

    @Nested
    class EnvVariablesTest {

        @Test
        void printEnvVariables() {
            Map<String, String> envs = System.getenv();
            envs.forEach((k, v) -> System.out.println(k + ":" + v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "DESKTOP_SESSION", matches = "ubuntu")
        void testDesktopSessionEnv() {

        }

        @Test
        @DisplayName("Test that the balance is set correctly in the account only when the server is dev")
        void test_balanceAccountDev() {
            boolean isDev = "dev".equals(System.getProperty("ENV"));
            assumeTrue(isDev);
            assertEquals(1000.12345, account.getBalance().doubleValue());
            assertFalse(account.getBalance().compareTo(BigDecimal.ZERO) < 0);
        }
    }

    @Tag("param")
    @Nested
    class ParametrizedTest {

        @RepeatedTest(value = 5, name = "Repeticion numero {currentRepetition} de {totalRepetitions}")
        @DisplayName("Test account debit money repeated")
        void test_accountDebitRepeated(RepetitionInfo info) {
            System.out.println(info.getCurrentRepetition());
            account.debit(new BigDecimal(100));
            assertNotNull(account.getBalance());
            assertEquals(900, account.getBalance().intValue());
            assertEquals("900.12345", account.getBalance().toPlainString());
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @ValueSource(strings = {"100", "200", "300", "500", "1000"})
        void test_accountDebitParametrizedValueSource(String amount) {
            account.debit(new BigDecimal(amount));
            assertNotNull(account.getBalance());
            assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvSource({"1,100", "2,200", "3,300", "4,500", "5,1000"})
        void test_accountDebitParametrizedCsvSource(String index, String amount) {
            System.out.println(index + "->" + amount);
            account.debit(new BigDecimal(amount));
            assertNotNull(account.getBalance());
            assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data.csv")
        void test_accountDebitParametrizedCsvFileSource(String amount) {
            account.debit(new BigDecimal(amount));
            assertNotNull(account.getBalance());
            assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @MethodSource("montoList")
        void test_accountDebitParametrizedMethodSource(String amount) {
            account.debit(new BigDecimal(amount));
            assertNotNull(account.getBalance());
            assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
        }

        static List<String> montoList() {
            return List.of("100", "200", "300", "400", "500");
        }

    }

    @Nested
    class TimeoutTests {
        @Test
        @Timeout(value = 1, unit = TimeUnit.SECONDS)
        void testTimeout() throws InterruptedException {
            TimeUnit.SECONDS.sleep(2);
        }

        @Test
        void testTimeoutAssertion() {
            assertTimeout(Duration.ofSeconds(5l), ()->{
                TimeUnit.MILLISECONDS.sleep(5500);
            });
        }

    }
}