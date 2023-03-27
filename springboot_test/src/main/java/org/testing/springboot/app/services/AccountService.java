package org.testing.springboot.app.services;

import org.testing.springboot.app.models.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.ListIterator;

public interface AccountService {

    List<Account> findAll();

    Account save(Account account);

    Account findById(Long id);

    int checkTotalTransfers(Long bankId);

    BigDecimal checkBalance(Long accountId);

    void transfer(Long sourceAccountId, Long destinationAccountId, BigDecimal amount, Long bankId);
}
