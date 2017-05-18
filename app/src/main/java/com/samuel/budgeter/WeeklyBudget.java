package com.samuel.budgeter;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class WeeklyBudget {
    DateTime startOfWeek;
    List<Expense> expenses;
    List<Income> incomeList;

    public WeeklyBudget(DateTime startOfWeek) {
        this.startOfWeek = startOfWeek;
        expenses = new ArrayList<>();
        incomeList = new ArrayList<>();
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public void addIncome(Income income) {
        Log.d("Income", "adding to week " + startOfWeek);
        incomeList.add(income);
        Log.d("Income", "amount " + income.getAmount());
    }

    public double getNetIncome() {
        Log.d("Income", "week " + startOfWeek);
        double totalIncome = 0;
        for(Income income: incomeList) {
            totalIncome += income.getAmount();
        }
        Log.d("Income", "total income " + totalIncome);
        double totalExpenses = 0;
        for(Expense expense: expenses) {
            totalExpenses += expense.getAmount();
        }
        Log.d("Income", "total expenses " + totalExpenses);
        Log.d("Income", "net " + (totalIncome - totalExpenses));
        return totalIncome - totalExpenses;
    }

    public DateTime getStartOfWeek() {
        return startOfWeek;
    }

    public boolean hasExpenses() {
        return expenses.size() > 0;
    }

    public boolean hasIncome() {
        return incomeList.size() > 0;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }
    public List<Income> getIncomeList() {
        return incomeList;
    }
}
