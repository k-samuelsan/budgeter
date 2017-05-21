package com.samuel.budgeter.core;

import org.joda.time.DateTime;

public class Expense {
    double amount;
    long dateInMillis;
    String details;

    public Expense(double amount, DateTime date, String details) {
        this.amount = amount;
        this.dateInMillis = date.getMillis();
        this.details = details;
    }

    public double getAmount() {
        return amount;
    }

    public long getDateInMillis() {
        return dateInMillis;
    }
}
