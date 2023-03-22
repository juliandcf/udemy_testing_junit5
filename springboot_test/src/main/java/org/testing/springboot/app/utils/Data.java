package org.testing.springboot.app.utils;

import org.testing.springboot.app.models.Account;
import org.testing.springboot.app.models.Bank;

import java.math.BigDecimal;

public class Data {

    public static Account createAccount001() {
        return new Account(1l, "Andres", new BigDecimal("1000"));
    }

    public static Account createAccount002() {
        return new Account(2l, "Jhon", new BigDecimal("2000"));
    }

    public static Bank createBank() {
        return new Bank(1l, "El banco financiero", 0);
    }
}
