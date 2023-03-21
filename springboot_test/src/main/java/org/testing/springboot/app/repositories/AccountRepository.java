package org.testing.springboot.app.repositories;

import org.testing.springboot.app.models.Account;

import java.util.List;

public interface AccountRepository {

    List<Account> findAll();

    Account findById(Long id);

    void update(Account account);


}
