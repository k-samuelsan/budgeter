package com.samuel.budgeter.core;

import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class WeeklyBudget {
    private DateTime startOfWeek;
    private List<Transaction> transactions;
    private static WeeklyBudget currentWeek;

    WeeklyBudget(DateTime startOfWeek) {
        this.startOfWeek = startOfWeek;
        transactions = new ArrayList<>();
    }

    void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public double getNetIncome() {
        double income = 0;
        for(Transaction transaction: transactions) {
            income += transaction.getAmount();
        }
        return income;
    }

    public DateTime getStartOfWeek() {
        return startOfWeek;
    }

    boolean hasTransactions() {
        return transactions.size() > 0;
    }

    List<Transaction> getTransactions() {
        return transactions;
    }

    public static void setCurrentWeek(WeeklyBudget week) {
        currentWeek = week;
    }

    public static WeeklyBudget getCurrentWeek() {
        return currentWeek;
    }
}
