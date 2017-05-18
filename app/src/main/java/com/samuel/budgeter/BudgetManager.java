package com.samuel.budgeter;

import android.content.Context;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class BudgetManager {
    private static BudgetManager budgetManager;
    private List<MonthlyBudget> monthlyBudgets;
    Context context;

    private BudgetManager(Context context) {
        monthlyBudgets = new ArrayList<>();
        this.context = context;
    }

    public static BudgetManager getInstance(Context context) {
        if(budgetManager == null) {
            budgetManager = new BudgetManager(context);
        }
        return budgetManager;
    }

    public void addExpense(Expense expense) {
        if(monthlyBudgets != null) {
            for(MonthlyBudget monthlyBudget: monthlyBudgets) {
                if(monthlyBudget.containsDate(expense.getDate())) {
                    monthlyBudget.addExpense(expense);
                    FileManager.getInstance(context).saveBudgetData();
                    return;
                }
            }
        }
        MonthlyBudget newMonthlyBudget = addMonthlyBudget(expense.getDate());
        newMonthlyBudget.addExpense(expense);
        FileManager.getInstance(context).saveBudgetData();
    }

    public void addIncome(Income income) {
        if(monthlyBudgets != null) {
            for(MonthlyBudget monthlyBudget: monthlyBudgets) {
                if(monthlyBudget.containsDate(income.getDateInMillisec())) {
                    monthlyBudget.addIncome(income);
                    FileManager.getInstance(context).saveBudgetData();
                    return;
                }
            }
        }
        MonthlyBudget newMonthlyBudget = addMonthlyBudget(income.getDateInMillisec());
        newMonthlyBudget.addIncome(income);
        FileManager.getInstance(context).saveBudgetData();
    }

    private MonthlyBudget addMonthlyBudget(long dateInMillisec) {
        Log.d("Debug", "making new monthly budget");
        DateTime date = new DateTime(dateInMillisec);
        MonthlyBudget monthlyBudget = new MonthlyBudget(date.withDayOfMonth(1).getMillis());
        monthlyBudgets.add(monthlyBudget);
        return monthlyBudget;
    }

    public double getCurrentMonthNetIncome() {
        MonthlyBudget currentMonth = MonthlyBudget.getCurrentMonth();
        if(currentMonth == null) {
            return 0;
        }
        return currentMonth.getNetIncome();
    }

    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        for(MonthlyBudget monthlyBudget: monthlyBudgets) {
            expenses.addAll(monthlyBudget.getAllExpenses());
        }
        return expenses;
    }

    public List<Income> getAllIncome() {
        List<Income> incomeList = new ArrayList<>();
        for(MonthlyBudget monthlyBudget: monthlyBudgets) {
            incomeList.addAll(monthlyBudget.getAllIncome());
        }
        return incomeList;
    }
}
