package org.testing.springboot.app.services;

import org.testing.springboot.app.models.Account;
import org.testing.springboot.app.models.Bank;
import org.testing.springboot.app.repositories.AccountRepository;
import org.testing.springboot.app.repositories.BankRepository;

import java.math.BigDecimal;

public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    private BankRepository bankRepository;

    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public int checkTotalTransfers(Long bankId) {
        Bank bank = bankRepository.findById(bankId);
        return bank.getTotalTransfers();
    }

    @Override
    public BigDecimal checkBalance(Long accountId) {
        Account account = accountRepository.findById(accountId);
        return account.getBalance();
    }

    @Override
    public void transfer(Long sourceAccountId, Long destinationAccountId, BigDecimal amount, Long bankId) {
        Account sourceAccount = accountRepository.findById(sourceAccountId);
        sourceAccount.debit(amount);
        accountRepository.update(sourceAccount);

        Account destinationAccount = accountRepository.findById(destinationAccountId);
        destinationAccount.credit(amount);
        accountRepository.update(destinationAccount);

        // El id del banco podria venir en la session, por eso se harcodea para este ejemplo
        Bank bank = bankRepository.findById(1l);
        bank.setTotalTransfers(bank.getTotalTransfers() + 1);
        bankRepository.update(bank);

    }
}
