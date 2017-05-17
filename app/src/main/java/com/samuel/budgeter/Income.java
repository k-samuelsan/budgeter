package com.samuel.budgeter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class Income {
    double amount;
    DateTime date;

    public Income(double amount, DateTime date) {
        this.amount = amount;
        this.date = date;
    }

    public double getAmount () {
        return amount;
    }

    public DateTime getDate() {
        return date;
    }
}
