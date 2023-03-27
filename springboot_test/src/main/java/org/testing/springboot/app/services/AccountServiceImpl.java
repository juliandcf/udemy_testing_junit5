package org.testing.springboot.app.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testing.springboot.app.models.Account;
import org.testing.springboot.app.models.Bank;
import org.testing.springboot.app.repositories.AccountRepository;
import org.testing.springboot.app.repositories.BankRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    private BankRepository bankRepository;

    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public int checkTotalTransfers(Long bankId) {
        Bank bank = bankRepository.findById(bankId).orElseThrow();
        return bank.getTotalTransfers();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal checkBalance(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        return account.getBalance();
    }

    @Override
    @Transactional
    public void transfer(Long sourceAccountId, Long destinationAccountId, BigDecimal amount, Long bankId) {
        Account sourceAccount = accountRepository.findById(sourceAccountId).orElseThrow();
        sourceAccount.debit(amount);
        accountRepository.save(sourceAccount);

        Account destinationAccount = accountRepository.findById(destinationAccountId).orElseThrow();
        destinationAccount.credit(amount);
        accountRepository.save(destinationAccount);

        // El id del banco podria venir en la session, por eso se harcodea para este ejemplo
        Bank bank = bankRepository.findById(1l).orElseThrow();
        bank.setTotalTransfers(bank.getTotalTransfers() + 1);
        bankRepository.save(bank);

    }
}
