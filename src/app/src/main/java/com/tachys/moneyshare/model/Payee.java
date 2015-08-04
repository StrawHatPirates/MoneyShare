package com.tachys.moneyshare.model;

/**
 * Created by tejom_000 on 26-07-2015.
 */
public class Payee {

    public String name;
    public double amountOwed;

    public Payee(String s, double o) {
        name = s;
        amountOwed = o;
    }
}
