package org.testing.springboot.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.testing.springboot.app.models.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByPerson(String person);
}
