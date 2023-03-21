package org.testing.springboot.app.repositories;

import org.testing.springboot.app.models.Bank;

import java.util.List;

public interface BankRepository {

    List<Bank> findAll();
    Bank findById(Long id);

    void update(Bank bank);
}
