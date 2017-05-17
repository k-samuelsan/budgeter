package com.samuel.budgeter;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class BudgetManager {
    private static BudgetManager budgetManager;
    private List<MonthlyBudget> monthlyBudgets;

    private BudgetManager() {
        monthlyBudgets = new ArrayList<>();
    }

    public static BudgetManager getInstance() {
        if(budgetManager == null) {
            budgetManager = new BudgetManager();
        }
        return budgetManager;
    }

    public void addExpense(Expense expense) {
        if(monthlyBudgets != null) {
            for(MonthlyBudget monthlyBudget: monthlyBudgets) {
                if(monthlyBudget.containsDate(expense.getDate())) {
                    monthlyBudget.addExpense(expense);
                    return;
                }
            }
        }
        MonthlyBudget newMonthlyBudget = addMonthlyBudget(expense.getDate());
        newMonthlyBudget.addExpense(expense);
    }

    public void addIncome(Income income) {
        if(monthlyBudgets != null) {
            for(MonthlyBudget monthlyBudget: monthlyBudgets) {
                if(monthlyBudget.containsDate(income.getDate())) {
                    monthlyBudget.addIncome(income);
                    return;
                }
            }
        }
        MonthlyBudget newMonthlyBudget = addMonthlyBudget(income.getDate());
        newMonthlyBudget.addIncome(income);

    }

    private MonthlyBudget addMonthlyBudget(DateTime date) {
        Log.d("Debug", "making new monthly budget");
        MonthlyBudget monthlyBudget = new MonthlyBudget(date.withDayOfMonth(1));
        monthlyBudgets.add(monthlyBudget);
        return monthlyBudget;
    }

    public double getCurrentMonthNetIncome() {
        MonthlyBudget currentMonth = MonthlyBudget.getCurrentMonth();
        if(currentMonth == null) {
            Log.d("Debug", "null here");
            return 0;
        }
        return currentMonth.getNetIncome();
    }
}
