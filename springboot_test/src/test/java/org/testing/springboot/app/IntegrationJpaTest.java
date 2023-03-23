package org.testing.springboot.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testing.springboot.app.models.Account;
import org.testing.springboot.app.repositories.AccountRepository;
import org.testing.springboot.app.repositories.BankRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IntegrationJpaTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BankRepository bankRepository;

    @Test
    void testFindById() {
        Optional<Account> account = accountRepository.findById(1l);

        assertTrue(account.isPresent());
        assertEquals("Andrés", account.get().getPerson());
    }

    @Test
    void testFindByPerson() {
        Optional<Account> account = accountRepository.findByPerson("Andrés");

        assertTrue(account.isPresent());
        assertEquals("Andrés", account.get().getPerson());
        assertEquals("1000.00", account.get().getBalance().toPlainString());
    }

    @Test
    void testSave() {
        Account account = new Account(null, "pepe", new BigDecimal("3000"));
        accountRepository.save(account);

        Account accountPepe = accountRepository.findByPerson("pepe").orElseThrow();
        assertEquals("pepe", accountPepe.getPerson());
        assertEquals(3l, accountPepe.getId());
    }
}
