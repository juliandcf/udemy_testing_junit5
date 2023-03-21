package org.testing.springboot.app.models;

import java.util.Objects;

public class Bank {

    private Long id;
    private String name;
    private int totalTransfers;

    public Bank(Long id, String name, int totalTransfers) {
        this.id = id;
        this.name = name;
        this.totalTransfers = totalTransfers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalTransfers() {
        return totalTransfers;
    }

    public void setTotalTransfers(int totalTransfers) {
        this.totalTransfers = totalTransfers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return totalTransfers == bank.totalTransfers && id.equals(bank.id) && name.equals(bank.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, totalTransfers);
    }
}
