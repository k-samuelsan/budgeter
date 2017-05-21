package com.samuel.budgeter.core;


import org.joda.time.DateTime;

public class Income {
    double amount;
    long dateInMillisec;

    public Income(double amount, DateTime date) {
        this.amount = amount;
        this.dateInMillisec = date.getMillis();
    }

    public double getAmount () {
        return amount;
    }

    public long getDateInMillis() {
        return dateInMillisec;
    }
}
