package com.samuel.budgeter.managers;
import java.util.Observable;
import android.util.Log;

import com.samuel.budgeter.core.Expense;
import com.samuel.budgeter.core.Income;
import com.samuel.budgeter.core.MonthlyBudget;
import com.samuel.budgeter.core.WeeklyBudget;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class BudgetManager extends Observable {
    private static BudgetManager budgetManager;
    private List<MonthlyBudget> monthlyBudgets;

    private BudgetManager() {
        monthlyBudgets = new ArrayList<>();
        DateTime now = new DateTime().withTime(0,0,0,0);
        long startOfMonthInMillisec = now.withDayOfMonth(1).getMillis();
        long startOfWeekInMillisec = now.withDayOfWeek(DateTimeConstants.MONDAY).getMillis();
        MonthlyBudget thisMonth = new MonthlyBudget(startOfMonthInMillisec);
        WeeklyBudget thisWeek = thisMonth.getWeekForDate(startOfWeekInMillisec);
        MonthlyBudget.setCurrentMonth(thisMonth);
        WeeklyBudget.setCurrentWeek(thisWeek);
        monthlyBudgets.add(thisMonth);
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
                    setChanged();
                    notifyObservers();
                    return;
                }
            }
        }
        MonthlyBudget newMonthlyBudget = addMonthlyBudget(expense.getDate());
        newMonthlyBudget.addExpense(expense);
        setChanged();
        notifyObservers();
    }

    public void addIncome(Income income) {
        if(monthlyBudgets != null) {
            for(MonthlyBudget monthlyBudget: monthlyBudgets) {
                if(monthlyBudget.containsDate(income.getDateInMillis())) {
                    monthlyBudget.addIncome(income);
                    setChanged();
                    notifyObservers();
                    return;
                }
            }
        }
        MonthlyBudget newMonthlyBudget = addMonthlyBudget(income.getDateInMillis());
        newMonthlyBudget.addIncome(income);
        setChanged();
        notifyObservers();
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

    public double getCurrentWeekNetIncome() {
        WeeklyBudget currentWeek = WeeklyBudget.getCurrentWeek();
        if(currentWeek == null) {
            return 0;
        }
        return currentWeek.getNetIncome();
    }

    List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        for(MonthlyBudget monthlyBudget: monthlyBudgets) {
            expenses.addAll(monthlyBudget.getAllExpenses());
        }
        return expenses;
    }

    List<Income> getAllIncome() {
        List<Income> incomeList = new ArrayList<>();
        for(MonthlyBudget monthlyBudget: monthlyBudgets) {
            incomeList.addAll(monthlyBudget.getAllIncome());
        }
        return incomeList;
    }
}
