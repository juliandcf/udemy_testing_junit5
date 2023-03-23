package org.testing.springboot.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.testing.springboot.app.models.Bank;

import java.util.List;

public interface BankRepository extends JpaRepository<Bank, Long> {

}
