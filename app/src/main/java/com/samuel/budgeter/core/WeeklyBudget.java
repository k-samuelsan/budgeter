package com.samuel.budgeter.core;

import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class WeeklyBudget {
    private DateTime startOfWeek;
    private List<Expense> expenses;
    private List<Income> incomeList;
    private static WeeklyBudget currentWeek;

    WeeklyBudget(DateTime startOfWeek) {
        this.startOfWeek = startOfWeek;
        expenses = new ArrayList<>();
        incomeList = new ArrayList<>();
    }

    void addExpense(Expense expense) {
        expenses.add(expense);
    }

    void addIncome(Income income) {
        Log.d("Income", "adding to week " + startOfWeek);
        incomeList.add(income);
        Log.d("Income", "amount " + income.getAmount());
    }

    public double getNetIncome() {
//        Log.d("Income", "week " + startOfWeek);
        double totalIncome = 0;
        for(Income income: incomeList) {
            totalIncome += income.getAmount();
        }
//        Log.d("Income", "total income " + totalIncome);
        double totalExpenses = 0;
        for(Expense expense: expenses) {
            totalExpenses += expense.getAmount();
        }
//        Log.d("Income", "total expenses " + totalExpenses);
//        Log.d("Income", "net " + (totalIncome - totalExpenses));
        return totalIncome - totalExpenses;
    }

    DateTime getStartOfWeek() {
        return startOfWeek;
    }

    boolean hasExpenses() {
        return expenses.size() > 0;
    }

    boolean hasIncome() {
        return incomeList.size() > 0;
    }

    List<Expense> getExpenses() {
        return expenses;
    }
    List<Income> getIncome() {
        return incomeList;
    }

    public static void setCurrentWeek(WeeklyBudget week) {
        currentWeek = week;
    }

    public static WeeklyBudget getCurrentWeek() {
        return currentWeek;
    }
}
