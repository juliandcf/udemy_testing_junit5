package org.testing.springboot.app.services;

import org.testing.springboot.app.models.Account;

import java.math.BigDecimal;

public interface AccountService {

    Account findById(Long id);

    int checkTotalTransfers(Long bankId);

    BigDecimal checkBalance(Long accountId);

    void transfer(Long sourceAccountId, Long destinationAccountId, BigDecimal amount, Long bankId);
}
