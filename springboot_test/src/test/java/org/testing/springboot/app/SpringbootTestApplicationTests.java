package org.testing.springboot.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testing.springboot.app.exceptions.NotEnoughMoneyException;
import org.testing.springboot.app.models.Account;
import org.testing.springboot.app.models.Bank;
import org.testing.springboot.app.repositories.AccountRepository;
import org.testing.springboot.app.repositories.BankRepository;
import org.testing.springboot.app.services.AccountService;
import org.testing.springboot.app.utils.Data;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpringbootTestApplicationTests {

    @MockBean
    AccountRepository accountRepository;

    @MockBean
    BankRepository bankRepository;

    @Autowired
    AccountService accountService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void contextLoads() {
        when(accountRepository.findById(1l)).thenReturn(Optional.of(Data.createAccount001()));
        when(accountRepository.findById(2l)).thenReturn(Optional.of(Data.createAccount002()));
        when(bankRepository.findById(1l)).thenReturn(Optional.of(Data.createBank()));

        BigDecimal balanceSource = accountService.checkBalance(1l);
        BigDecimal balanceDestination = accountService.checkBalance(2l);

        assertEquals("1000", balanceSource.toPlainString());
        assertEquals("2000", balanceDestination.toPlainString());

        accountService.transfer(1l, 2l, new BigDecimal("100"), 1l);

        balanceSource = accountService.checkBalance(1l);
        balanceDestination = accountService.checkBalance(2l);

        assertEquals("900", balanceSource.toPlainString());
        assertEquals("2100", balanceDestination.toPlainString());

        int total = accountService.checkTotalTransfers(1l);
        assertEquals(1, total);

        verify(accountRepository, times(3)).findById(1l);
        verify(accountRepository, times(3)).findById(2l);
        verify(accountRepository, times(2)).save(any(Account.class));

        verify(bankRepository, times(2)).findById(1l);
        verify(bankRepository).save(any(Bank.class));

        verify(accountRepository, times(6)).findById(anyLong());
        verify(accountRepository, never()).findAll();
    }

    @Test
    void contextLoads2() {
        when(accountRepository.findById(1l)).thenReturn(Optional.of(Data.createAccount001()));
        when(accountRepository.findById(2l)).thenReturn(Optional.of(Data.createAccount002()));
        when(bankRepository.findById(1l)).thenReturn(Optional.of(Data.createBank()));

        BigDecimal balanceSource = accountService.checkBalance(1l);
        BigDecimal balanceDestination = accountService.checkBalance(2l);

        assertEquals("1000", balanceSource.toPlainString());
        assertEquals("2000", balanceDestination.toPlainString());

        assertThrows(NotEnoughMoneyException.class, () -> {
            accountService.transfer(1l, 2l, new BigDecimal("1200"), 1l);
        });

        balanceSource = accountService.checkBalance(1l);
        balanceDestination = accountService.checkBalance(2l);

        assertEquals("1000", balanceSource.toPlainString());
        assertEquals("2000", balanceDestination.toPlainString());

        int total = accountService.checkTotalTransfers(1l);
        assertEquals(0, total);

        verify(accountRepository, times(3)).findById(1l);
        verify(accountRepository, times(2)).findById(2l);
        verify(accountRepository, never()).save(any(Account.class));

        verify(bankRepository).findById(1l);
        verify(bankRepository, never()).save(any(Bank.class));
    }

    @Test
    void contextLoads3() {
        when(accountRepository.findById(1l)).thenReturn(Optional.of(Data.createAccount001()));

        Account account1 = accountService.findById(1l);
        Account account2 = accountService.findById(1l);

        assertSame(account1, account2);
        verify(accountRepository, times(2)).findById(anyLong());

    }
}
