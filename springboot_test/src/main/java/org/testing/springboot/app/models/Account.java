package org.testing.springboot.app.models;

import org.testing.springboot.app.exceptions.NotEnoughMoneyException;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {

    private Long id;
    private String person;
    private BigDecimal balance;

    public Account(Long id, String person, BigDecimal balance) {
        this.id = id;
        this.person = person;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    public void debit(BigDecimal amount) {
        BigDecimal newBalance = this.balance.subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0 )
            throw new NotEnoughMoneyException("Dinero insuficiente en la cuenta");

        this.balance = newBalance;
    }

    public void credit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id.equals(account.id) && person.equals(account.person) && balance.equals(account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, balance);
    }
}
