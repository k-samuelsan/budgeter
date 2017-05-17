package com.samuel.budgeter;

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
        incomeList.add(income);
    }

    public double getNetIncome() {
        double totalIncome = 0;
        for(Income income: incomeList) {
            totalIncome += income.getAmount();
        }
        double totalExpenses = 0;
        for(Expense expense: expenses) {
            totalExpenses += expense.getAmount();
        }
        return totalIncome - totalExpenses;
    }

    public DateTime getStartOfWeek() {
        return startOfWeek;
    }
}
