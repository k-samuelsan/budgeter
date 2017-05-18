package com.samuel.budgeter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class Expense {
    double amount;
    long dateInMillisec;
    String details;

    public Expense(double amount, DateTime date, String details) {
        this.amount = amount;
        this.dateInMillisec = date.getMillis();
        this.details = details;
    }

    public double getAmount() {
        return amount;
    }

    public long getDate() {
        return dateInMillisec;
    }
}
