package org.testing.springboot.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.testing.springboot.app.exceptions.NotEnoughMoneyException;
import org.testing.springboot.app.models.Account;
import org.testing.springboot.app.models.Bank;
import org.testing.springboot.app.repositories.AccountRepository;
import org.testing.springboot.app.repositories.BankRepository;
import org.testing.springboot.app.services.AccountService;
import org.testing.springboot.app.services.AccountServiceImpl;
import org.testing.springboot.app.utils.Data;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpringbootTestApplicationTests {

	@Mock
	AccountRepository accountRepository;

	@Mock
	BankRepository bankRepository;

	@InjectMocks
	AccountServiceImpl accountService;

	@BeforeEach
	void setUp() {

	}

	@Test
	void contextLoads() {
		when(accountRepository.findById(1l)).thenReturn(Data.createAccount001());
		when(accountRepository.findById(2l)).thenReturn(Data.createAccount002());
		when(bankRepository.findById(1l)).thenReturn(Data.createBank());

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
		verify(accountRepository, times(2)).update(any(Account.class));

		verify(bankRepository, times(2)).findById(1l);
		verify(bankRepository).update(any(Bank.class));
	}

	@Test
	void contextLoads2() {
		when(accountRepository.findById(1l)).thenReturn(Data.createAccount001());
		when(accountRepository.findById(2l)).thenReturn(Data.createAccount002());
		when(bankRepository.findById(1l)).thenReturn(Data.createBank());

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
		verify(accountRepository, never()).update(any(Account.class));

		verify(bankRepository).findById(1l);
		verify(bankRepository, never()).update(any(Bank.class));
	}

}
