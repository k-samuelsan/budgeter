package com.samuel.budgeter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class Expense {
    double amount;
    DateTime date;
    String details;

    public Expense(double amount, DateTime date, String details) {
        this.amount = amount;
        this.date = date;
        this.details = details;
    }

    public double getAmount() {
        return amount;
    }

    public DateTime getDate() {
        return date;
    }
}
