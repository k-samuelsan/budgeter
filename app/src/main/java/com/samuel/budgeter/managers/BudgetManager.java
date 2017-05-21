package com.samuel.budgeter.managers;
import java.util.Observable;

import android.content.Context;
import android.util.Log;

import com.samuel.budgeter.core.Expense;
import com.samuel.budgeter.core.Income;
import com.samuel.budgeter.core.MonthlyBudget;
import com.samuel.budgeter.core.WeeklyBudget;
import com.samuel.budgeter.core.YearlyBudget;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class BudgetManager extends Observable {
    private static final DateTime NOW = new DateTime().withTime(0,0,0,0);
    private static final DateTime START_OF_CURRENT_YEAR = NOW.withDayOfYear(1);
    private static final DateTime START_OF_CURRENT_MONTH = NOW.withDayOfMonth(1);
    private static final DateTime START_OF_CURRENT_WEEK = NOW.withDayOfWeek(DateTimeConstants.MONDAY);
    private static BudgetManager budgetManager;
    private YearlyBudget loadedYear;

    private BudgetManager() {
        YearlyBudget thisYear = new YearlyBudget(START_OF_CURRENT_YEAR);
        MonthlyBudget thisMonth = thisYear.getMonthForDate(START_OF_CURRENT_MONTH);
        WeeklyBudget thisWeek = thisMonth.getWeekForDate(START_OF_CURRENT_WEEK);
        YearlyBudget.setCurrentYear(thisYear);
        MonthlyBudget.setCurrentMonth(thisMonth);
        WeeklyBudget.setCurrentWeek(thisWeek);
        loadedYear = thisYear;
    }

    public static BudgetManager getInstance() {
        if(budgetManager == null) {
            budgetManager = new BudgetManager();
        }
        return budgetManager;
    }

    public void addExpense(Expense expense, Context context) {
        if(!loadedYear.containsDate(expense.getDateInMillis())) {
            Log.d("SWITCH", "switching years: " + new DateTime(expense.getDateInMillis()));
            DateTime startOfNewYear = new DateTime(expense.getDateInMillis()).withDayOfYear(1).withTime(0,0,0,0);
            FileManager.getInstance().loadYear(startOfNewYear, context);
        }
        loadedYear.addExpense(expense);
        setChanged();
        notifyObservers();
        if(context != null) {
            FileManager.getInstance().saveBudgetData(context);
        }
    }

    public void addIncome(Income income, Context context) {
        if(!loadedYear.containsDate(income.getDateInMillis())) {
            FileManager.getInstance().loadYear(new DateTime(income.getDateInMillis()), context);
        }
        loadedYear.addIncome(income);
        setChanged();
        notifyObservers();
        if(context != null) {
            FileManager.getInstance().saveBudgetData(context);
        }
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
        expenses.addAll(loadedYear.getExpenses());
        return expenses;
    }

    List<Income> getAllIncome() {
        List<Income> incomeList = new ArrayList<>();
        incomeList.addAll(loadedYear.getIncome());
        return incomeList;
    }

    long getStartOfLoadedYearInMillis() {
        return loadedYear.getStartOfYear().getMillis();
    }

    void setLoadedYear(YearlyBudget yearlyBudget) {
        this.loadedYear = yearlyBudget;
        if(loadedYear.getStartOfYear().isEqual(START_OF_CURRENT_YEAR)) {
            MonthlyBudget thisMonth = loadedYear.getMonthForDate(START_OF_CURRENT_MONTH);
            WeeklyBudget thisWeek = thisMonth.getWeekForDate(START_OF_CURRENT_WEEK);
            YearlyBudget.setCurrentYear(loadedYear);
            MonthlyBudget.setCurrentMonth(thisMonth);
            WeeklyBudget.setCurrentWeek(thisWeek);
        }
    }
}
